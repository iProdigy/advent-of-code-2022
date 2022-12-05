fun main() {
    fun part1(input: List<String>): String {
        val (stacks, instructions) = parseInput(input)

        instructions.forEach {
            val from = stacks[it.fromIndex]
            val to = stacks[it.toIndex]

            (1..it.quantity)
                .map { from.removeFirst() }
                .forEach { c -> to.addFirst(c) }
        }

        return output(stacks)
    }

    fun part2(input: List<String>): String {
        val (stacks, instructions) = parseInput(input)

        instructions.forEach {
            val from = stacks[it.fromIndex]
            val to = stacks[it.toIndex]

            (1..it.quantity)
                .map { from.removeFirst() }
                .reversed()
                .forEach { c -> to.addFirst(c) }
        }

        return output(stacks)
    }

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

private val instructionRegex = Regex("^move (\\d+) from (\\d+) to (\\d+)\$")

private fun parseInstruction(str: String) = instructionRegex.matchEntire(str)!!.groupValues.drop(1).map { it.toInt() }
    .let { Instruction(it[0], it[1] - 1, it[2] - 1) }

private data class Instruction(val quantity: Int, val fromIndex: Int, val toIndex: Int)
