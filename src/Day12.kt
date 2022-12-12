fun main() {
    fun part1(input: List<String>): Int {
        val (start, end, map) = parse(input)
        return findShortest(start, end, map, heuristic = Point2D::manhattanDist)!!.size - 1
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }.toTypedArray()
        val end = grid.map { it.indexOf('E') }.withIndex().first { it.value >= 0 }.let { it.index to it.value }
        return breadthFirstSearch(end, { normalize(grid[it.first][it.second]) == 'a' }) {
            it.neighbors(maxX = grid.size, maxY = grid[0].size)
                .filter { from -> isConnected(from, it, grid) }
        }!!.size - 1
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test")
    check(part1(testInput) == 31)
    check(part2(testInput) == 29)

    val input = readInput("Day12")
    println(part1(input)) // 394
    println(part2(input)) // 388
}

private fun parse(input: List<String>): Triple<Point2D, Point2D, Map<Point2D, Collection<Point2D>>> {
    val grid: Array<CharArray> = input.map { it.toCharArray() }.toTypedArray()
    val map: MutableMap<Point2D, Collection<Point2D>> = hashMapOf()
    var start: Point2D? = null
    var end: Point2D? = null

    for (i in grid.indices) {
        val row = grid[i]
        for (j in row.indices) {
            val value = row[j]
            val max = normalize(value) + 1
            val point = i to j

            if (start == null && value == 'S') {
                start = point
            } else if (end == null && value == 'E') {
                end = point
            }

            val candidates = point.neighbors(maxX = grid.size, maxY = row.size).filter { isConnected(point, it, grid, max) }
            map[point] = candidates
        }
    }

    return Triple(start!!, end!!, map)
}

private fun isConnected(from: Point2D, to: Point2D, grid: Array<CharArray>, max: Char = normalize(grid[from.first][from.second]) + 1) =
    normalize(grid[to.first][to.second]) <= max

private fun normalize(it: Char) = if (it == 'S') 'a' else if (it == 'E') 'z' else it
