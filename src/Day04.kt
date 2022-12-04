fun main() {
    fun part1(input: List<String>): Int {
        return parse(input).count { (a, b) -> a containsAll b || b containsAll a }
    }

    fun part2(input: List<String>): Int {
        return parse(input).count { (a, b) -> a containsAny b }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("Day04")
    println(part1(input)) // 657
    println(part2(input)) // 938
}

private fun parse(input: List<String>) = input
    .map { it.split(',') }
    .map { (left, right) -> convert(left) to convert(right) }

private fun convert(part: String) = part.split('-').let { it[0].toInt()..it[1].toInt() }
