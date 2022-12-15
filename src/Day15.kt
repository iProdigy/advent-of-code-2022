import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>, targetY: Int = 2000000): Int {
        var minX = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE

        val parsed = input.map(::parse)

        parsed.forEach { (sensor, beacon) ->
            val dist = sensor.manhattanDist(beacon)
            minX = min(minX, sensor.first - dist)
            maxX = max(maxX, sensor.first + dist)
            minX = min(minX, beacon.first - dist)
            maxX = max(maxX, beacon.first + dist)
        }

        return (minX..maxX).map { it to targetY }.count {
            parsed.any { (sensor, beacon) ->
                val minDist = sensor.manhattanDist(beacon)
                val dist = it.manhattanDist(sensor)
                it != sensor && it != beacon && dist <= minDist
            }
        }
    }

    fun part2(input: List<String>): Long {
        val parsed = input.map(::parse)

        var minX = Int.MAX_VALUE
        var maxX = Int.MIN_VALUE
        var minY = Int.MAX_VALUE
        var maxY = Int.MIN_VALUE

        parsed.forEach { (sensor, _) ->
            minX = min(minX, sensor.first)
            maxX = max(maxX, sensor.first)
            minY = min(minY, sensor.second)
            maxY = max(maxY, sensor.second)
        }

        val xRange = max(0, minX)..min(maxX, 4000000)
        val yRange = max(0, minY)..min(maxY, 4000000)

        return parsed.asSequence()
            .map { (sensor, beacon) -> ring(sensor, beacon) }
            .flatten()
            .filter { it.first in xRange && it.second in yRange }
            .first {
                parsed.all { (sensor, beacon) ->
                    val minDist = sensor.manhattanDist(beacon)
                    val dist = it.manhattanDist(sensor)
                    it != sensor && it != beacon && dist > minDist
                }
            }
            .let { 4000000L * it.first + it.second }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput, targetY = 10) == 26)
    check(part2(testInput) == 56000011L)

    val input = readInput("Day15")
    println(part1(input)) // 5461729
    println(part2(input)) // 10621647166538
}

private fun ring(sensor: Point2D, beacon: Point2D): Sequence<Point2D> = sequence {
    val dist = sensor.manhattanDist(beacon) + 1
    for (yOffset in 0..dist) {
        val xOffset = dist - yOffset
        yield(sensor.first + xOffset to sensor.second + yOffset)

        if (xOffset != 0) {
            yield(sensor.first - xOffset to sensor.second + yOffset)
        }

        if (yOffset != 0) {
            yield(sensor.first + xOffset to sensor.second - yOffset)
            yield(sensor.first - xOffset to sensor.second - yOffset)
        }
    }
}

private val regex = """^Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)${'$'}""".toRegex()

private fun parse(line: String): Pair<Point2D, Point2D> = regex.matchEntire(line)!!
    .groupValues
    .drop(1)
    .map { it.toInt() }
    .let { (x, y, x1, y1) -> (x to y) to (x1 to y1) }
