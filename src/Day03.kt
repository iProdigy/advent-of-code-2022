fun main() {
    fun part1(input: List<String>): Int = input
        .map { it.chunked(it.length / 2, CharSequence::toSet) }
        .map { (a, b) -> a.first { it in b } }
        .sumOf(::priority)

    fun part2(input: List<String>): Int = input
        .map { it.toSet() }
        .chunked(3)
        .map { (a, b, c) -> a.first { it in b && it in c } }
        .sumOf(::priority)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input)) // 7742
    println(part2(input)) // 2276
}

private fun priority(c: Char): Int = 1 + if (c in 'a'..'z') c - 'a' else c - 'A' + 26
