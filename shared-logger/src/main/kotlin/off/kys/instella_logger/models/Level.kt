package off.kys.instella_logger.models

enum class Level(val priority: Int, val colorCode: String) {
    DEBUG(10, "\u001B[36m"),   // Cyan
    INFO(20, "\u001B[32m"),    // Green
    WARN(30, "\u001B[33m"),    // Yellow
    ERROR(40, "\u001B[31m")    // Red
}