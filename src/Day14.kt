import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        val (grid, xRange) = parse(input)
        return simulate(grid, xRange.first, xRange.second, null)
    }

    fun part2(input: List<String>): Int {
        val (grid, x, maxY) = parse(input)
        grid[maxY + 2].run {
            indices.forEach { this[it] = true }
        }
        return simulate(grid, x.first, x.second, maxY + 2)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput) == 24)
    check(part2(testInput) == 93)

    val input = readInput("Day14")
    println(part1(input)) // 638
    println(part2(input)) // 31722
}

private fun simulate(grid: Array<BooleanArray>, minX: Int, maxX: Int, maxY: Int? = null): Int {
    var dropped = 0

    while (true) {
        // attempt to drop sand
        val (x, y) = drop(grid) ?: break

        // ensure placement is within bounds
        if (maxY == null && (x < minX || x > maxX)) break

        // if so, indicate placed
        grid[y][x] = true
        dropped++
    }

    return dropped
}

private fun drop(grid: Array<BooleanArray>, sourceX: Int = 500, sourceY: Int = 0): Pair<Int, Int>? {
    var x = sourceX
    var y = sourceY

    if (grid[y][x] && grid[y + 1][x - 1] && grid[y + 1][x + 1]) return null

    while (y + 1 < grid.size && (!grid[y + 1][x] || !grid[y + 1][x - 1] || !grid[y + 1][x + 1])) {
        while (!(grid.getOrNull(y + 1) ?: return null)[x]) {
            y++
        }

        if (!grid[y + 1][x - 1]) {
            y++
            x--
        } else if (!grid[y + 1][x + 1]) {
            y++
            x++
        } else {
            break
        }
    }

    return x to y
}

private fun parse(input: List<String>): Triple<Array<BooleanArray>, Pair<Int, Int>, Int> {
    val grid = Array(200) { BooleanArray(1000) }

    var minX = Int.MAX_VALUE
    var maxX = Int.MIN_VALUE
    var maxY = 0

    input.map { it.split(" -> ").map { coordinate -> coordinate.split(',').map { num -> num.toInt() }.let { (a, b) -> a to b } } }
        .map { it.windowed(2) }
        .flatten()
        .forEach { (a, b) ->
            val xRange = min(a.first, b.first)..max(a.first, b.first)
            val yRange = min(a.second, b.second)..max(a.second, b.second)

            for (x in xRange) {
                for (y in yRange) {
                    grid[y][x] = true
                }
            }

            minX = min(minX, xRange.first)
            maxX = max(maxX, xRange.last)
            maxY = max(maxY, yRange.last)
        }

    return Triple(grid, minX to maxX, maxY)
}
