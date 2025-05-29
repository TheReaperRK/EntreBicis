package cat.copernic.frontend.profile_management.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun SwipeToCollect(
    modifier: Modifier = Modifier,
    onCollected: () -> Unit,
    collected: Boolean
) {
    val swipeDistance = 240f // distancia necesaria para completar
    val swipeState = remember { Animatable(0f) }

    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .height(56.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(50))
            .background(if (collected) Color.Gray else Color(0xFF166C4E))
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    val newPos = (swipeState.value + delta).coerceIn(0f, swipeDistance)
                    scope.launch { swipeState.snapTo(newPos) }
                },
                onDragStopped = {
                    if (swipeState.value >= swipeDistance * 0.9f) {
                        onCollected()
                        scope.launch { swipeState.snapTo(swipeDistance) }
                    } else {
                        scope.launch { swipeState.animateTo(0f) }
                    }
                }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset { IntOffset(swipeState.value.roundToInt(), 0) }
                .padding(start = 12.dp)
        ) {
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.White)
        }

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(
                text = if (collected) "Recollida" else "Desplaça per recollir",
                color = Color.White
            )
        }
    }
}
