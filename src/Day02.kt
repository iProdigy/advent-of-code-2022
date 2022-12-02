fun main() {
    fun part1(input: List<String>): Int = input.asSequence()
        .map { arrayOf(it.first(), it.last()) }
        .map {
            val move = it[1] - 'X' + 1 // [1, 3]

            val round = when ((it[1] - 'X') - (it[0] - 'A')) {
                0 -> 3 // draw
                1, -2 -> 6 // win
                else -> 0 // loss
            }

            move + round
        }
        .sum()

    fun part2(input: List<String>): Int = input.asSequence()
        .map { arrayOf(it.first(), it.last()) }
        .map {
            val round = (it[1] - 'X') * 3 // 0, 3, 6

            val other = it[0] - 'A' // [0, 2]
            val move = when (round) {
                0 -> other - 1 // lose
                6 -> other + 1 // win
                else -> other // draw
            }.mod(3) + 1 // put in [1, 3]

            move + round
        }
        .sum()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("Day02")
    println(part1(input)) // 11150
    println(part2(input)) // 8295
}
