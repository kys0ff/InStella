package kys.off.instella.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun PackagesTableHeaderDivider(color: Color = Color(0xFFC4C4C2)) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(0.7.dp)
            .background(color)
    )
}