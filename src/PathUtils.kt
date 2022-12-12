import java.util.PriorityQueue
import kotlin.collections.ArrayDeque

fun <T> breadthFirstSearch(start: T, end: T, edges: Map<T, Collection<T>>) = breadthFirstSearch(start, { it == end }, { edges[it] })

fun <T> breadthFirstSearch(start: T, end: (T) -> Boolean, edges: (T) -> Collection<T>?): List<T>? {
    val queue = ArrayDeque<T>().apply { this += start }
    val seen = hashSetOf<T>().apply { this += start }
    val from = hashMapOf<T, T>()

    while (queue.isNotEmpty()) {
        val current = queue.removeFirst()
        if (end(current))
            return reconstruct(current, from)

        edges(current)?.forEach {
            if (seen.add(it)) {
                from[it] = current
                queue.addLast(it)
            }
        }
    }

    return null
}

fun <T> findShortest(
    start: T,
    end: T,
    edges: Map<T, Collection<T>>,
    weight: (T, T) -> Int = { _, _ -> 1 },
    heuristic: (T, T) -> Int = { _, _ -> 0 }
): List<T>? {
    val gScore = hashMapOf<T, Int>().apply { this[start] = 0 }
    val fScore = hashMapOf<T, Int>().apply { this[start] = heuristic(start, end) }

    val open = PriorityQueue<T>(edges.size, compareBy { fScore.computeIfAbsent(it) { p -> heuristic(p, end) } }).apply { this += start }
    val from = hashMapOf<T, T>()

    while (open.isNotEmpty()) {
        val current = open.poll()
        if (current == end)
            return reconstruct(current, from)

        edges[current]?.forEach {
            val tentative = gScore.getOrDefault(current, Int.MAX_VALUE) + weight(current, it)
            if (tentative < gScore.getOrDefault(it, Int.MAX_VALUE)) {
                from[it] = current
                gScore[it] = tentative
                fScore[it] = tentative + heuristic(it, end)
                if (it !in open)
                    open += it
            }
        }
    }

    return null
}

private fun <T> reconstruct(last: T, from: Map<T, T>): List<T> {
    val path = ArrayDeque<T>()
    var current: T? = last
    while (current != null) {
        path.addFirst(current)
        current = from[current]
    }
    return path
}
