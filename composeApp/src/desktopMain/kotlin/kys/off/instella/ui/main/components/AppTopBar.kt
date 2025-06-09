package kys.off.instella.ui.main.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AppTopBar(title: String = "InStella") {
    Box(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFFECEAEB))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 14.5.sp,
            fontWeight = FontWeight.Bold
        )
    }
    Box(
        Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Color(0xFFD7D5D5))
    )
    Box(
        Modifier
            .fillMaxWidth()
            .height(0.5.dp)
            .background(Color(0xFFE2E1E1))
    )
}