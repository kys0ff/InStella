package off.kys.pakly

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.draganddrop.dragAndDropTarget
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget
import androidx.compose.ui.draganddrop.DragData
import androidx.compose.ui.draganddrop.dragData
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kys.off.instella.ui.utils.dashedBorder
import java.io.File
import java.net.URI
import kotlin.io.path.toPath

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun DragDropArea(
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Gray,
    backgroundColor: Color = Color.Transparent,
    borderStroke: Dp = 1.5.dp,
    cornerRadius: Dp = 12.dp,
    onFilesDropped: (List<File>) -> Unit,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    var isDragOver by remember { mutableStateOf(false) }

    val dragAndDropTarget = remember {
        object : DragAndDropTarget {
            override fun onStarted(event: DragAndDropEvent) {
                isDragOver = true
            }

            override fun onEntered(event: DragAndDropEvent) {
                isDragOver = true
            }

            override fun onExited(event: DragAndDropEvent) {
                isDragOver = false
            }

            override fun onDrop(event: DragAndDropEvent): Boolean {
                isDragOver = false

                val files = runCatching {
                    (event.dragData() as? DragData.FilesList)
                        ?.readFiles()
                        ?.mapNotNull { path ->
                            try {
                                URI(path).toPath().toFile()
                            } catch (e: Exception) {
                                e.printStackTrace()
                                null
                            }
                        }.orEmpty()
                }.getOrElse {
                    it.printStackTrace()
                    emptyList()
                }

                return if (files.isNotEmpty()) {
                    onFilesDropped(files)
                    true
                } else {
                    false
                }
            }

            override fun onEnded(event: DragAndDropEvent) {
                isDragOver = false
            }
        }
    }

    Box(
        modifier = modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .background(if (isDragOver) backgroundColor.copy(alpha = 0.2f) else backgroundColor)
            .dashedBorder(color = borderColor, strokeWidth = borderStroke, dashLength = 6.dp)
            .height(78.dp)
            .dragAndDropTarget(
                shouldStartDragAndDrop = { true },
                target = dragAndDropTarget
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            content = content
        )
    }
}