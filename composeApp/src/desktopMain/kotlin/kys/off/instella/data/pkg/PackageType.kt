package kys.off.instella.data.pkg

enum class PackageType {
    GZ, RPM, JAR, GIT, DEB,

    @Suppress("SpellCheckingInspection")
    APPIMAGE;

    private val formattedName: String
        get() = name.replace(".", "_").lowercase()

    operator fun invoke() = formattedName

    companion object {
        fun of(value: String): PackageType? = entries.firstOrNull { type ->
            type.name == value.replace(".", "_").uppercase()
        }
    }
}