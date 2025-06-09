package kys.off.instella.data.pkg

enum class PackageType {
    TAR_GZ, RPM, JAR, GIT, DEB, APP_IMAGE;

    val formattedName: String
        get() = name.replace(".", "_").lowercase()

    companion object {
        fun of(value: String): PackageType? = entries.firstOrNull { it.name == value.replace(".", "_").uppercase() }
    }
}