package kys.off.instella.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RowScope.PackagesTableRowItem(title: String, isLastTitle: Boolean = false) {
    Text(
        modifier = Modifier
            .fillMaxHeight()
            .padding(4.dp)
            .padding(start = 6.dp)
            .padding(top = 1.5.dp)
            .weight(1f),
        text = title,
        fontSize = 14.5.sp
    )
    if (isLastTitle.not()) Box(
        Modifier
            .fillMaxHeight()
            .width(0.5.dp)
            .background(Color.LightGray)
    )
}