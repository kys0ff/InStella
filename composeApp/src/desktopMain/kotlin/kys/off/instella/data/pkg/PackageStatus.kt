package kys.off.instella.data.pkg

enum class PackageStatus {
    PENDING, INSTALLED, CANCELED, UNINSTALLED, INSTALLING;

    val formattedName: String
        get() = name.toCharArray().mapIndexed { i, c-> if (i > 0) c.lowercase() else c.uppercase() }.joinToString(separator = "")

}