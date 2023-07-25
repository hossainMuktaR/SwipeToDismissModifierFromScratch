package com.example.swipetodismissmodifierfromscratch

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.unit.IntOffset
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

fun Modifier.swipeToDismiss(
    onDismissed: () -> Unit ): Modifier = composed {
    val animOffsetX = remember { Animatable(0f) }
    pointerInput(Unit){
        val decay = splineBasedDecay<Float>(this)
        coroutineScope {
            while(true){
                val pointerId = awaitPointerEventScope { awaitFirstDown().id }
                val velocityTracker = VelocityTracker()
                // wait for drag event
                awaitPointerEventScope {
                    horizontalDrag(pointerId) { change ->
                        val horizontalDragOffset = animOffsetX.value + change.positionChange().x
                        launch {
                            animOffsetX.snapTo(horizontalDragOffset)
                        }
                        velocityTracker.addPosition(change.uptimeMillis, change.position)
                        if(change.positionChange() != Offset.Zero) change.consume()
                    }
                }
                val velocity = velocityTracker.calculateVelocity().x
                val targetOffsetX = decay.calculateTargetValue(animOffsetX.value, velocity)
                animOffsetX.updateBounds(
                    lowerBound = - size.width.toFloat(),
                    upperBound =  size.width.toFloat()
                )
                launch {
                    if (targetOffsetX.absoluteValue <= size.width){
                        animOffsetX.animateTo(targetValue = 0f, initialVelocity = velocity)
                    } else {
                        animOffsetX.animateDecay(velocity, decay)
                        onDismissed()
                    }
                }
            }
        }
    }
        .offset { IntOffset(animOffsetX.value.roundToInt(), 0) }
}