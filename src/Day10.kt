import java.lang.StringBuilder
import java.util.concurrent.atomic.AtomicInteger

fun main() {
    fun part1(input: List<String>): Int = AtomicInteger().apply {
        run(input) { cycle, x ->
            if ((cycle - 20) % 40 == 0)
                this += (cycle * x)
        }
    }.get()

    fun part2(input: List<String>) = StringBuilder().apply {
        run(input) { cycle, x ->
            this += when {
                (cycle - 1) % 40 in x - 1..x + 1 -> '#'
                else -> '.'
            }
        }
    }.toString().chunked(40).joinToString(separator = "\n")

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 13140)
    check(
        part2(testInput) == """##..##..##..##..##..##..##..##..##..##..
###...###...###...###...###...###...###.
####....####....####....####....####....
#####.....#####.....#####.....#####.....
######......######......######......####
#######.......#######.......#######....."""
    )

    val input = readInput("Day10")
    println(part1(input)) // 11720
    println(part2(input)) // ERCREPCJ
}

private fun run(input: List<String>, consumer: (Int, Int) -> Unit) {
    var x = 1
    var cycle = 0
    var next: Int? = null

    val queue = input.mapTo(ArrayDeque()) { it.split(' ') }
    while (queue.isNotEmpty()) {
        cycle++
        consumer(cycle, x)
        if (next != null) {
            x += next
            next = null
        } else {
            val it = queue.removeFirst()
            if ("addx" == it[0])
                next = it[1].toInt()
        }
    }
}
