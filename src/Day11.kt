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
    val monkeys = parse(input).associateBy { it.id }
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

private fun parse(input: List<String>) = input.partitionBy { it.isEmpty() }
    .map { it.drop(1).map { line -> line.substring(line.indexOf(':') + 2) } }
    .mapIndexed { index, (startingStr, operationStr, testStr, trueStr, falseStr) ->
        fun last(str: String) = str.substring(str.lastIndexOf(' ') + 1)
        val items = startingStr.split(", ").mapTo(ArrayDeque()) { it.toLong() }

        val opDelim = operationStr.lastIndexOf(' ')
        val opNum = operationStr.substring(opDelim + 1).toLongOrNull()
        val operation: (Long) -> Long = when {
            operationStr.endsWith("* old") -> { it -> it * it }
            '*' == operationStr[opDelim - 1] -> { it -> it * opNum!! }
            '+' == operationStr[opDelim - 1] -> { it -> it + opNum!! }
            else -> throw Error("Bad Input!")
        }

        Monkey(index, items, last(testStr).toLong(), last(trueStr).toInt(), last(falseStr).toInt(), operation)
    }

private data class Monkey(
    val id: Int,
    val items: ArrayDeque<Long> = ArrayDeque(),
    val testDivisible: Long,
    val nextWhenTrue: Int,
    val nextWhenFalse: Int,
    val operation: (Long) -> Long
)
