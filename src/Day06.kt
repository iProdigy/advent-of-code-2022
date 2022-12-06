fun main() {
    fun solve(input: String, n: Int) = input
        .windowed(n)
        .withIndex()
        .dropWhile { it.value.toSet().size < n }
        .first()
        .index + n

    fun part1(input: String) = solve(input, 4)
    fun part2(input: String) = solve(input, 14)

    // test if implementation meets criteria from the description, like:
    check(part1("bvwbjplbgvbhsrlpgdmjqwftvncz") == 5)
    check(part1("nppdvjthqldpwncqszvftbrmjlhg") == 6)
    check(part1("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 10)
    check(part1("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 11)
    check(part2("mjqjpqmgbljsphdztnvjfqwrcgsmlb") == 19)
    check(part2("bvwbjplbgvbhsrlpgdmjqwftvncz") == 23)
    check(part2("nppdvjthqldpwncqszvftbrmjlhg") == 23)
    check(part2("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 29)
    check(part2("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 26)

    val input = readFullInput("Day06")
    println(part1(input)) // 1093
    println(part2(input)) // 3534
}
