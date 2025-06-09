package kys.off.instella.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NativeButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val buttonShape = RoundedCornerShape(6.dp)

    Box(
        modifier = modifier
            .clip(buttonShape)
            .background(color = Color(0xFFF5F4F3), shape = buttonShape)
            .border(width = 0.7.dp, color = Color(0xFFC3C4C3), shape = buttonShape)
            .clickable(onClick = onClick)  // Changed to standard clickable
    ) {
        Text(
            modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 6.dp, top = 6.dp),
            text = text,
            textAlign = TextAlign.Center,
            fontSize = 15.sp
        )
    }
}