package kys.off.instella.data.pkg

data class PackageFile(
    val path: String,
    val fileName: String,
    val type: PackageType,
    val name: String,
    val version: String,
    val size: String,
    val status: PackageStatus = PackageStatus.PENDING,
)