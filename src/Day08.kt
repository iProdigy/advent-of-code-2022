import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }.map { chars -> chars.map { it.digitToInt() } }
        val width = grid.first().size
        val length = grid.size
        val visible = Array(length) { BooleanArray(width) }

        fun directionalRunningMax(
            rowInterval: IntProgression = 0 until length,
            colInterval: IntProgression = 0 until width,
            rowOffset: Int = 0,
            colOffset: Int = 0,
            startingEdge: (Int, Int) -> Boolean
        ) = Array(length) { IntArray(width) }.apply {
            for (row in rowInterval) {
                for (col in colInterval) {
                    if (startingEdge(row, col)) {
                        visible[row][col] = true
                        this[row][col] = grid[row][col]
                    } else {
                        this[row][col] = max(this[row + rowOffset][col + colOffset], grid[row + rowOffset][col + colOffset])
                    }
                }
            }
        }

        val ltrMax = directionalRunningMax(colOffset = -1) { _, col -> col == 0 }
        val rtlMax = directionalRunningMax(colInterval = width - 1 downTo 0, colOffset = 1) { _, col -> col + 1 == width }
        val ttbMax = directionalRunningMax(rowOffset = -1) { row, _ -> row == 0 }
        val bttMax = directionalRunningMax(rowInterval = length - 1 downTo 0, rowOffset = 1) { row, _ -> row + 1 == length }
        val maxes = listOf(ltrMax, rtlMax, ttbMax, bttMax)

        for (row in 1 until length - 1) {
            for (col in 1 until width - 1) {
                val height = grid[row][col]
                val minMax = maxes.minOf { it[row][col] }
                if (height > minMax)
                    visible[row][col] = true
            }
        }

        return visible.sumOf { it.count { b -> b } }
    }

    fun part2(input: List<String>): Int {
        val grid = input.map { it.toCharArray() }.map { chars -> chars.map { it.digitToInt() } }
        val width = grid.first().size
        val length = grid.size

        var scenic = 0
        for (row in 1 until length - 1) {
            for (col in 1 until width - 1) {
                val height = grid[row][col]

                fun outwardCount(rowOffset: Int = 0, colOffset: Int = 0, range: IntProgression): Int {
                    var c = 1
                    if (height > grid[row + rowOffset][col + colOffset]) {
                        c += range.takeWhileAndOneMore {
                            grid[if (rowOffset != 0) it else row][if (rowOffset != 0) col else it] < height
                        }.count()
                    }
                    return c
                }

                val top = outwardCount(-1, 0, row - 2 downTo 0)
                val left = outwardCount(0, -1, col - 2 downTo 0)
                val right = outwardCount(0, 1, col + 2 until width)
                val bottom = outwardCount(1, 0, row + 2 until length)
                scenic = max(scenic, top * left * right * bottom)
            }
        }

        return scenic
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day08_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readInput("Day08")
    println(part1(input)) // 1859
    println(part2(input)) // 332640
}
