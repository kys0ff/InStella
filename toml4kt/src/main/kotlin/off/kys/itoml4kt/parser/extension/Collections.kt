package off.kys.itoml4kt.parser.extension

inline fun <T, reified N> Iterable<T>.forEachOfType(action: (N) -> Unit) {
    for (element in this) (element as? N)?.let(action)
}