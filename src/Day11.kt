fun main() {
    fun part1(input: List<String>) = run(input, 20, true)
    fun part2(input: List<String>) = run(input, 10_000, false)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readInput("Day11")
    println(part1(input)) // 57838
    println(part2(input)) // 15050382231
}

private fun run(input: List<String>, rounds: Int, divideBy3: Boolean): Long {
    val monkeys = if (input.size < 30) testMonkeys() else realMonkeys() // TODO: parse from input
    val divisorProduct = monkeys.values.map { it.testDivisible }.distinct().reduce(Long::times)
    val counts = IntArray(monkeys.size)

    for (round in 1..rounds) {
        monkeys.values.forEach {
            while (it.items.isNotEmpty()) {
                val item = it.items.removeFirst()
                val updated = it.operation(item).let { l -> if (divideBy3) l / 3 else l } % divisorProduct
                counts[it.id]++

                val next = if (updated % it.testDivisible == 0L) it.nextWhenTrue else it.nextWhenFalse
                monkeys[next]!!.items += updated
            }
        }
    }

    return counts.sortedDescending().let { (first, second) -> first.toLong() * second }
}

private fun realMonkeys() = listOf(
    Monkey(0, ArrayDeque(listOf(77, 69, 76, 77, 50, 58)), 5, 1, 5) { it * 11 },
    Monkey(1, ArrayDeque(listOf(75, 70, 82, 83, 96, 64, 62)), 17, 5, 6) { it + 8 },
    Monkey(2, ArrayDeque(listOf(53)), 2, 0, 7) { it * 3 },
    Monkey(3, ArrayDeque(listOf(85, 64, 93, 64, 99)), 7, 7, 2) { it + 4 },
    Monkey(4, ArrayDeque(listOf(61, 92, 71)), 3, 2, 3) { it * it },
    Monkey(5, ArrayDeque(listOf(79, 73, 50, 90)), 11, 4, 6) { it + 2 },
    Monkey(6, ArrayDeque(listOf(50, 89)), 13, 4, 3) { it + 3 },
    Monkey(7, ArrayDeque(listOf(83, 56, 64, 58, 93, 91, 56, 65)), 19, 1, 0) { it + 5 },
).associateBy { it.id }

private fun testMonkeys() = listOf(
    Monkey(0, ArrayDeque(listOf(79, 98)), 23, 2, 3) { it * 19 },
    Monkey(1, ArrayDeque(listOf(54, 65, 75, 74)), 19, 2, 0) { it + 6 },
    Monkey(2, ArrayDeque(listOf(79, 60, 97)), 13, 1, 3) { it * it },
    Monkey(3, ArrayDeque(listOf(74)), 17, 0, 1) { it + 3 },
).associateBy { it.id }

private data class Monkey(
    val id: Int,
    val items: ArrayDeque<Long> = ArrayDeque(),
    val testDivisible: Long,
    val nextWhenTrue: Int,
    val nextWhenFalse: Int,
    val operation: (Long) -> Long
)
