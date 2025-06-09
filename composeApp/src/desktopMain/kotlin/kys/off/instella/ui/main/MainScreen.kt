package kys.off.instella.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import kys.off.instella.ui.components.NativeButton
import kys.off.instella.ui.main.components.AppTopBar
import kys.off.instella.ui.main.components.PackagesTableHeaderDivider
import kys.off.instella.ui.main.components.PackagesTableRowHeader
import kys.off.instella.ui.main.components.PackagesTableRowItem
import kys.off.instella.utils.extensions.getFormattedSize
import kys.off.instella.utils.file_picker.filePicker
import off.kys.pakly.DragDropArea

@Composable
fun MainScreen() {
    val packageFiles = remember { mutableStateListOf<PackageFile>() }
    val pkgsTableShape = RoundedCornerShape(4.5.dp)
    val pkgsTableBorderColor = Color(0xFFC4C4C2)

    Column(
        Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
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
                    title { "Pick package files" }
                    filter { "*.tar.gz" }
                    filter { "*.rpm" }
                    allowMultiple { true }
                    onSelect { pkgs ->
                        pkgs.forEach { pkg ->
                            val type = PackageType.of(pkg.extension) ?: run {
                                println("Unsupported package format: ${pkg.extension}")
                                return@onSelect
                            }

                            packageFiles += PackageFile(
                                path = pkg.absolutePath,
                                fileName = pkg.nameWithoutExtension,
                                type = type,
                                name = pkg.nameWithoutExtension,
                                version = "1.0.0v",
                                size = pkg.getFormattedSize(),
                                status = PackageStatus.PENDING
                            )
                        }
                    }
                }
            }
        }

        Spacer(Modifier.size(6.dp))

        DragDropArea(
            onFilesDropped = {

            }
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "Drag and drop your .tar.gz, .AppImage .deb or .rpm files here",
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
                items(items = packageFiles) { pkg ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(32.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PackagesTableRowItem(pkg.fileName)
                        PackagesTableRowItem(pkg.type.formattedName)
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
            NativeButton(text = "Install (Local)", Modifier.padding(top = 4.dp, bottom = 4.dp)) {

            }

            Spacer(Modifier.size(16.dp))

            NativeButton(text = "Install (Global)", Modifier.padding(top = 4.dp, bottom = 4.dp)) {

            }
        }
    }
}