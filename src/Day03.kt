fun main() {
    fun part1(input: List<String>): Int = input.asSequence()
        .map {
            val split = it.length / 2
            it.substring(0, split) to it.substring(split)
        }
        .map { (first, second) ->
            val seen = first.toCharArray().toSet()
            second.toCharArray().filterTo(mutableSetOf()) { it in seen }
        }
        .flatten()
        .sumOf(::priority)

    fun part2(input: List<String>): Int = input
        .chunked(3)
        .map {
            val first = it[0].toCharArray().toSet()
            val second = it[1].toCharArray().toSet()
            it[2].toCharArray().first { c -> c in first && c in second }
        }
        .sumOf(::priority)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("Day03")
    println(part1(input)) // 7742
    println(part2(input)) // 2276
}

fun priority(c: Char): Int = 1 + if (c in 'a'..'z') c - 'a' else c - 'A' + 26
