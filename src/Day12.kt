fun main() {
    fun part1(input: List<String>): Int {
        val (start, end, map) = parse(input)
        return findShortest(start, end, map, heuristic = Point2D::manhattanDist)!!.size - 1
    }

    fun part2(input: List<String>): Int {
        val (_, end, map) = parse(input)
        val starts = input.map { it.toCharArray() }
            .flatMapIndexed { i, chars -> chars.mapIndexed { j, c -> (i to j) to c } }
            .filter { (_, char) -> char == 'S' || char == 'a' }
            .map { it.first }

        return starts.minOf { findShortest(it, end, map, heuristic = Point2D::manhattanDist)?.size ?: Int.MAX_VALUE } - 1
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

            val candidates = listOf(i - 1 to j, i + 1 to j, i to j - 1, i to j + 1)
                .filter { (i2, j2) -> i2 >= 0 && j2 >= 0 && i2 < grid.size && j2 < row.size }
                .filter { (i2, j2) -> normalize(grid[i2][j2]) <= max }

            map[point] = candidates
        }
    }

    return Triple(start!!, end!!, map)
}

private fun normalize(it: Char) = if (it == 'S') 'a' else if (it == 'E') 'z' else it
