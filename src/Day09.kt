import kotlin.math.abs

fun main() {
    fun part1(input: List<String>) = run(input, 2)
    fun part2(input: List<String>) = run(input, 10)

    // test if implementation meets criteria from the description, like:
    check(part1(readInput("Day09_test")) == 13)
    check(part2(readInput("Day09_test2")) == 36)

    val input = readInput("Day09")
    println(part1(input)) // 6266
    println(part2(input)) // 2369
}

private fun run(input: List<String>, totalKnots: Int): Int {
    val knots = (0 until totalKnots).map { 0 to 0 }.toMutableList()
    val tailVisited = hashSetOf<Pair<Int, Int>>()

    input.map { it.split(' ', limit = 2) }
        .forEach { (dir, quantity) ->
            repeat(quantity.toInt()) {
                val oldHead = knots.first()
                knots[0] = when (dir) {
                    "U" -> oldHead.first to oldHead.second - 1
                    "D" -> oldHead.first to oldHead.second + 1
                    "L" -> oldHead.first - 1 to oldHead.second
                    "R" -> oldHead.first + 1 to oldHead.second
                    else -> throw Error("Bad Input!")
                }

                for (i in 1 until knots.size) {
                    val prev = knots[i - 1]
                    val curr = knots[i]
                    if (prev touching curr) break

                    val deltaX = (prev.first - curr.first).let { if (it > 0) 1 else if (it < 0) -1 else 0 }
                    val deltaY = (prev.second - curr.second).let { if (it > 0) 1 else if (it < 0) -1 else 0 }
                    knots[i] = curr.first + deltaX to curr.second + deltaY
                }

                tailVisited += knots.last()
            }
        }

    return tailVisited.count()
}

private infix fun Pair<Int, Int>.touching(other: Pair<Int, Int>) = abs(first - other.first) <= 1 && abs(second - other.second) <= 1
