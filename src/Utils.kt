import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

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

fun <T> Iterable<T>.partitionBy(shouldSplit: (T) -> Boolean): List<MutableList<T>> = fold(mutableListOf(mutableListOf())) { acc, t ->
    if (shouldSplit(t)) acc.add(mutableListOf()) else acc.last() += t
    return@fold acc
}
