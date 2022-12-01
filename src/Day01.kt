fun main() {
    fun part1(input: List<String>): Int {
        return computeCalories(input).max()
    }

    fun part2(input: List<String>): Int {
        return computeCalories(input).sortedDescending().take(3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readInput("Day01")
    println(part1(input)) // 74198
    println(part2(input)) // 209914
}

private fun computeCalories(input: List<String>) = mutableListOf<Int>().apply {
    var current = 0
    input.forEach {
        if (it.isEmpty()) {
            this += current
            current = 0
        } else {
            current += it.toInt()
        }
    }
    this += current
}
