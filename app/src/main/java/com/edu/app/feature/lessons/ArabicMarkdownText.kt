package com.edu.app.feature.lessons

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun ArabicMarkdownText(markdown: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        markdown.lines().forEach { raw ->
            val line = raw.trimEnd()
            when {
                line.isBlank() -> Spacer(Modifier.height(4.dp))
                line.startsWith("# ") -> Text(line.removePrefix("# "),
                    style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth())
                line.startsWith("## ") -> Text(line.removePrefix("## "),
                    style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth())
                line.startsWith("> ") -> Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()
                ) {
                    Text(renderInline(line.removePrefix("> ")),
                        style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Right,
                        modifier = Modifier.padding(12.dp).fillMaxWidth())
                }
                line.startsWith("- ") -> Row(Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End) {
                    Text(renderInline(line.removePrefix("- ")),
                        style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Right)
                    Spacer(Modifier.width(6.dp))
                    Text("•", style = MaterialTheme.typography.bodyLarge)
                }
                else -> Text(renderInline(line),
                    style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Right,
                    modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

private fun renderInline(text: String): AnnotatedString = buildAnnotatedString {
    text.split("**").forEachIndexed { i, part ->
        if (i % 2 == 1) withStyle(SpanStyle(fontWeight = FontWeight.Bold)) { append(part) }
        else append(part)
    }
}
