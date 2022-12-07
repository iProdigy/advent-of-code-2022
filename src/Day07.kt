import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Long {
        val root = parse(input)
        var sum = 0L
        fun run(folder: Folder) {
            if (folder.computedSize!! <= 100000) {
                sum += folder.computedSize!!
            }
            folder.subDirectories.forEach { run(it) }
        }
        run(root)
        return sum
    }

    fun part2(input: List<String>): Long {
        val root = parse(input)
        val unused = 70000000L - root.computedSize!!
        val delta = max(30000000 - unused, 0)

        var candidate: Folder? = null
        fun run(folder: Folder) {
            if (folder.computedSize!! > delta) {
                if (candidate == null || folder.computedSize!! < candidate!!.computedSize!!) {
                    candidate = folder
                }
            }

            folder.subDirectories.forEach { run(it) }
        }
        run(root)

        return candidate!!.computedSize!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 95437L)
    check(part2(testInput) == 24933642L)

    val input = readInput("Day07")
    println(part1(input)) // 1667443
    println(part2(input)) // 8998590
}

private fun parse(input: List<String>): Folder {
    val root = Folder(null, "")

    var current = root
    input
        .drop(1)
        .partitionBy(keepDelim = true) { it.startsWith('$') }
        .drop(1)
        .map { it.first().substring(2) to it.drop(1).map { r -> r.split(' ', limit = 2) } }
        .forEach { (command, result) ->
            if (command.startsWith("ls")) {
                result.forEach { (a, name) ->
                    if ("dir" == a) {
                        current.subDirectories += Folder(current, name)
                    } else {
                        current.files += File(current, a.toLong(), name)
                    }
                }
            } else if (command.startsWith("cd ..")) {
                current = current.parent!!
            } else if (command.startsWith("cd ")) {
                val folderName = command.substring("cd ".length)
                current = current.subDirectories.first { it.name == folderName }
            }
        }

    // depth first search to calculate folder sizes
    fun calculateSize(folder: Folder): Long {
        val fileSum = folder.files.sumOf { it.size }
        val folderSum = folder.subDirectories.sumOf { calculateSize(it) }
        val sum = fileSum + folderSum
        folder.computedSize = sum
        return sum
    }
    calculateSize(root)

    return root
}

private data class File(val parent: Folder?, val size: Long, val name: String)

private data class Folder(
    val parent: Folder?,
    val name: String,
    val files: MutableList<File> = mutableListOf(),
    val subDirectories: MutableList<Folder> = mutableListOf(),
    var computedSize: Long? = null
)
