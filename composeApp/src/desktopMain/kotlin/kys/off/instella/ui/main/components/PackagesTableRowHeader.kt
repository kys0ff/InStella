package kys.off.instella.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PackagesTableRowHeader() {
    Row(
        Modifier
            .fillMaxWidth()
            .height(32.dp)
            .background(Color(0xFFF2F1EF)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        PackagesTableRowItem("File Name")
        PackagesTableRowItem("Type")
        PackagesTableRowItem("Package Name")
        PackagesTableRowItem("Version")
        PackagesTableRowItem(title = "Status", isLastTitle = true)
    }
    PackagesTableHeaderDivider()
}