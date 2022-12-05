fun main() {
    fun solve(input: List<String>, reverse: Boolean = false): String {
        val (stacks, instructions) = parseInput(input)

        instructions.forEach {
            val from = stacks[it.fromIndex]
            val to = stacks[it.toIndex]

            (1..it.quantity)
                .map { from.removeFirst() }
                .run { if (reverse) reversed() else this }
                .forEach { c -> to.addFirst(c) }
        }

        return output(stacks)
    }

    fun part1(input: List<String>) = solve(input)
    fun part2(input: List<String>) = solve(input, true)

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("Day05")
    println(part1(input)) // SBPQRSCDF
    println(part2(input)) // RGLVRCQSB
}

private fun output(stacks: List<ArrayDeque<Char>>) = stacks.mapNotNull { it.firstOrNull() }.joinToString(separator = "")

private fun parseInput(input: List<String>): Pair<List<ArrayDeque<Char>>, List<Instruction>> {
    val (initialStacks, instructions) = input.partitionBy { it.isBlank() }
    val n = initialStacks.last().trim().replace("   ", ",").split(',').size
    return parseStacks(n, initialStacks) to instructions.map(::parseInstruction)
}

private fun parseStacks(n: Int, initialStacks: List<String>) = (1..n).map { ArrayDeque<Char>() }.apply {
    initialStacks.asSequence()
        .take(initialStacks.size - 1)
        .map { line -> line.chunked(4) { it[1] }.withIndex() }
        .flatten()
        .filter { it.value != ' ' }
        .forEach { this[it.index].addLast(it.value) }
}

private fun parseInstruction(str: String) = str.split(' ')
    .let { Instruction(it[1].toInt(), it[3].toInt() - 1, it[5].toInt() - 1) }

private data class Instruction(val quantity: Int, val fromIndex: Int, val toIndex: Int)
