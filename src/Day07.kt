import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Long {
        val root = parse(input)
        var sum = 0L
        fun run(folder: Folder) {
            if (folder.size <= 100000L)
                sum += folder.size
            folder.subDirectories.forEach { run(it) }
        }
        run(root)
        return sum
    }

    fun part2(input: List<String>): Long {
        val root = parse(input)
        val delta = max(30000000L - (70000000L - root.size), 0L)
        var candidate: Folder? = null
        fun run(folder: Folder) {
            if (folder.size >= delta && (candidate == null || folder.size < candidate!!.size))
                candidate = folder
            folder.subDirectories.forEach { run(it) }
        }
        run(root)
        return candidate!!.size
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
    input.partitionBy(keepDelim = true) { it.startsWith('$') }
        .dropWhile { it.isEmpty() }
        .map { it.first().substring(2) to it.drop(1).map { r -> r.split(' ', limit = 2) } }
        .forEach { (cmd, result) ->
            when (cmd) {
                "cd /" -> current = root
                "cd .." -> current = current.parent!!
                "ls" -> result.forEach { (info, name) ->
                    when (info) {
                        "dir" -> current.subDirectories += Folder(current, name)
                        else -> current.files += File(current, info.toLong(), name)
                    }
                }

                else -> cmd.substring("cd ".length).run { current = current.subDirectories.first { it.name == this } }
            }
        }

    return root
}

private data class File(val parent: Folder?, val size: Long, val name: String)

private data class Folder(
    val parent: Folder?,
    val name: String,
    val files: MutableList<File> = mutableListOf(),
    val subDirectories: MutableList<Folder> = mutableListOf(),
) {
    val size: Long by lazy { files.sumOf(File::size) + subDirectories.sumOf(Folder::size) }
}
