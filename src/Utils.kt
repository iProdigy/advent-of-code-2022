import java.io.File
import java.lang.StringBuilder
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.abs

typealias Point2D = Pair<Int, Int>

fun Point2D.manhattanDist(other: Point2D): Int = abs(this.first - other.first) + abs(this.second - other.second)

fun Point2D.euclideanDist(other: Point2D): Int = (this.first - other.first).let { it * it } + (this.second - other.second).let { it * it }

fun Point2D.neighbors(minX: Int = 0, minY: Int = 0, maxX: Int = Int.MAX_VALUE, maxY: Int = Int.MAX_VALUE, diagonals: Boolean = false) = mutableListOf(-1 to 0, +1 to 0, 0 to -1, 0 to +1)
    .apply { if (diagonals) addAll(listOf(-1 to -1, -1 to +1, +1 to +1, +1 to -1)) }
    .map { this.first + it.first to this.second + it.second }
    .filter { it.first in minX until maxX && it.second in minY until maxY }

/**
 * Obtains the input text file
 */
fun inputFile(name: String) = File("src", "$name.txt")

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = inputFile(name).readLines()

/**
 * Reads all of the lines from the input text file into a single string.
 */
fun readFullInput(name: String) = inputFile(name).readText()

/**
 * Performs the specified operation on each line from the input text file.
 */
fun <T> useInput(name: String, block: (Sequence<String>) -> T) = inputFile(name).useLines(block = block)

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

inline fun <T> Iterable<T>.partitionBy(keepDelim: Boolean = false, shouldSplit: (T) -> Boolean): List<List<T>> = fold(mutableListOf(mutableListOf())) { acc: MutableList<MutableList<T>>, t: T ->
    val split = shouldSplit(t)
    if (split)
        acc.add(mutableListOf())

    if (!split || keepDelim)
        acc.last() += t

    return@fold acc
}

inline fun <T> Iterable<T>.takeWhileAndOneMore(predicate: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (item in this) {
        list.add(item)
        if (!predicate(item))
            break
    }
    return list
}

infix fun IntRange.containsAll(other: IntRange) = this.first <= other.first && this.last >= other.last

infix fun IntRange.containsAny(other: IntRange) = this.first <= other.last && this.last >= other.first

operator fun StringBuilder.plusAssign(c: Char) {
    this.append(c)
}

operator fun AtomicInteger.plusAssign(i: Int) {
    this.addAndGet(i)
}
