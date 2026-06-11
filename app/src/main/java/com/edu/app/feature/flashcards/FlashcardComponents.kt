package com.edu.app.feature.flashcards

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.edu.app.domain.srs.ReviewGrade

@Composable
fun FlipCard(
    front: String,
    back: String,
    hint: String?,
    isRevealed: Boolean,
    onFlip: () -> Unit,
    modifier: Modifier = Modifier
) {
    val rotation by animateFloatAsState(
        targetValue = if (isRevealed) 180f else 0f,
        animationSpec = tween(450), label = "flip"
    )
    val density = LocalDensity.current.density
    Card(
        modifier = modifier.fillMaxWidth().heightIn(min = 260.dp)
            .graphicsLayer { rotationY = rotation; cameraDistance = 12f * density }
            .clickable(enabled = !isRevealed, onClick = onFlip),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
            if (rotation <= 90f) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("السؤال", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary)
                    Spacer(Modifier.height(12.dp))
                    Text(front, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
                    if (!hint.isNullOrBlank()) {
                        Spacer(Modifier.height(16.dp))
                        Text("💡 $hint", style = MaterialTheme.typography.labelSmall)
                    }
                    Spacer(Modifier.height(20.dp))
                    Text("اضغط لعرض الإجابة", style = MaterialTheme.typography.labelSmall)
                }
            } else {
                Column(Modifier.graphicsLayer { rotationY = 180f },
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("الإجابة", style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary)
                    Spacer(Modifier.height(12.dp))
                    Text(back, style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

@Composable
fun GradeBar(onGrade: (ReviewGrade) -> Unit, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        GradeButton("نسيت", Color(0xFFD64545), Modifier.weight(1f)) { onGrade(ReviewGrade.AGAIN) }
        GradeButton("صعب", Color(0xFFE08A2B), Modifier.weight(1f)) { onGrade(ReviewGrade.HARD) }
        GradeButton("جيد", Color(0xFF2D8A5F), Modifier.weight(1f)) { onGrade(ReviewGrade.GOOD) }
        GradeButton("سهل", Color(0xFF1E6F5C), Modifier.weight(1f)) { onGrade(ReviewGrade.EASY) }
    }
}

@Composable
private fun GradeButton(label: String, color: Color, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick, modifier = modifier,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(vertical = 14.dp)
    ) { Text(label, style = MaterialTheme.typography.labelSmall) }
}
