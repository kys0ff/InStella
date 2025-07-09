package kys.off.instella.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kys.off.instella.data.pkg.PackageFile
import kys.off.instella.data.pkg.PackageStatus
import kys.off.instella.data.pkg.PackageType
import kys.off.instella.plugins.PluginsProvider
import kys.off.instella.ui.components.NativeButton
import kys.off.instella.ui.main.components.AppTopBar
import kys.off.instella.ui.main.components.PackagesTableHeaderDivider
import kys.off.instella.ui.main.components.PackagesTableRowHeader
import kys.off.instella.ui.main.components.PackagesTableRowItem
import kys.off.instella.utils.extensions.getFormattedSize
import kys.off.instella.utils.file_picker.filePicker
import off.kys.instella_logger.Logger
import off.kys.instella_logger.models.UseCase
import off.kys.pakly.DragDropArea
import java.io.File

@Composable
fun MainScreen() {
    val packageFiles = remember { mutableStateListOf<PackageFile>() }
    val pkgsTableShape = RoundedCornerShape(4.5.dp)
    val pkgsTableBorderColor = Color(0xFFC4C4C2)

    var selectedPkg by remember { mutableIntStateOf(-1) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F8F6))
    ) {
        AppTopBar()

        Spacer(Modifier.size(10.dp))

        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            NativeButton("Add Package") {
                filePicker {
                    title { "Select Package Files" }
                    filter { "*.tar.gz" }
                    filter { "*.gz" }
                    filter { "*.rpm" }
                    allowMultiple { true }
                    onSelect { it.parseTo(packageFiles) }
                }
            }
        }

        Spacer(Modifier.size(6.dp))

        DragDropArea(
            onFilesDropped = { it.parseTo(packageFiles) }
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Drag and drop your .tar.gz, .AppImage, .deb, or .rpm files here to begin",
                textAlign = TextAlign.Center,
                fontSize = 16.5.sp
            )
        }

        Column(
            Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(16.dp)
                .clip(pkgsTableShape)
                .border(width = 0.7.dp, color = pkgsTableBorderColor, shape = pkgsTableShape)
        ) {
            PackagesTableRowHeader()

            LazyColumn(Modifier.fillMaxWidth()) {
                itemsIndexed(items = packageFiles) { index, pkg ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(32.dp)
                            .background(if (selectedPkg == index) Color(0xFF3399FF) else Color.Transparent)
                            .clickable { selectedPkg = if (selectedPkg == index) -1 else index },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PackagesTableRowItem(pkg.fileName)
                        PackagesTableRowItem(pkg.type())
                        PackagesTableRowItem(pkg.name)
                        PackagesTableRowItem(pkg.version)
                        PackagesTableRowItem(title = pkg.status.formattedName, isLastTitle = true)
                    }
                    PackagesTableHeaderDivider()
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Top
        ) {
            NativeButton(text = "Install for Current User", Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                // TODO: Handle local installation
            }

            Spacer(Modifier.size(16.dp))

            NativeButton(text = "Install for All Users", Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                // TODO: Handle global installation
            }
        }
    }
}

private fun List<File>.parseTo(
    packageFiles: SnapshotStateList<PackageFile>,
) {
    for (pkg in this) {
        val type = PackageType.of(pkg.extension) ?: run {
            Logger.error(
                message = "Unsupported package format: ${pkg.extension} --skipping",
                useCase = UseCase.INSTALLER
            )
            continue
        }

        PluginsProvider().getPluginIfSupports(pkg.extension)

        packageFiles += PackageFile(
            path = pkg.absolutePath,
            fileName = pkg.nameWithoutExtension,
            type = type,
            name = pkg.nameWithoutExtension,
            version = "1.0.0v",
            size = pkg.getFormattedSize(),
            status = PackageStatus.PENDING
        )
        Logger.info(
            message = "${pkg.name} has been added to queue",
            useCase = UseCase.SYSTEM
        )
    }
}