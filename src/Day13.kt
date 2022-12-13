fun main() {
    fun part1(input: List<String>) = input
        .partitionBy { it.isEmpty() }
        .mapIndexed { index, (first, second) -> if (check(first, second)) index + 1 else 0 }
        .sum()

    fun part2(input: List<String>) = input
        .filterTo(mutableListOf("[[2]]", "[[6]]")) { it.isNotEmpty() }
        .sortedWith { x, y -> check(parse(x), parse(y), true).let { b -> if (b == null) 0 else if (b) -1 else 1 } }
        .let { (it.indexOf("[[2]]") + 1) * (it.indexOf("[[6]]") + 1) }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 140)

    val input = readInput("Day13")
    println(part1(input)) // 5625
    println(part2(input)) // 23111
}

private fun check(first: String, second: String): Boolean = false != check(parse(first), parse(second), true)

private fun check(first: ListNode, second: ListNode, root: Boolean = false): Boolean? {
    while (second.value.isNotEmpty()) {
        val left = first.value.removeFirstOrNull() ?: return true
        val right = second.value.removeFirst()
        if (left is NumericNode && right is NumericNode) {
            if (left.value < right.value) return true
            if (right.value < left.value) return false
        } else {
            val b = check(left.asListNode(), right.asListNode())
            if (b != null) return b
        }
    }
    return if (first.value.isNotEmpty() || root) false else null
}

private fun parse(line: String): ListNode {
    val root = ListNode()
    var current: ListNode? = null

    val sb = StringBuilder(line)
    while (sb.isNotEmpty()) {
        val c = sb.first()
        var consumed = 1

        when (c) {
            '[' -> current = if (current == null) {
                root
            } else {
                val next = ListNode(current)
                current.value += next
                next
            }

            ']' -> current = current?.parent
            ',' -> Unit
            else -> {
                val num = sb.takeWhile { it.isDigit() }.toString()
                consumed = num.length
                val node = NumericNode(num.toInt())
                current!!.value += node
            }
        }

        sb.deleteRange(0, consumed)
    }

    return root
}

private fun Node<*>.asListNode(): ListNode = if (this is ListNode) this else ListNode().also { it.value += this }

private class ListNode(val parent: ListNode? = null) : Node<MutableList<Node<*>>>(mutableListOf())
private class NumericNode(value: Int) : Node<Int>(value)
private sealed class Node<T>(val value: T)
