fun main() {
    fun part1(input: List<String>): Int = input
        .map { (it.first() - 'A') to (it.last() - 'X') }
        .sumOf { (first, second) ->
            val move = second + 1 // [1, 3]

            val round = when (second - first) {
                0 -> 3 // draw
                1, -2 -> 6 // win
                else -> 0 // loss
            }

            move + round
        }

    fun part2(input: List<String>): Int = input
        .map { (it.first() - 'A') to (it.last() - 'X') }
        .sumOf { (first, second) ->
            val round = second * 3 // 0, 3, 6

            val move = when (round) {
                0 -> first - 1 // lose
                6 -> first + 1 // win
                else -> first // draw
            }.mod(3) + 1 // put in [1, 3]

            move + round
        }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input)) // 11150
    println(part2(input)) // 8295
}
