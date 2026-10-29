package com.mentorship.compose.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mentorship.compose.ui.theme.Accent100
import com.mentorship.compose.ui.theme.Accent2_100
import com.mentorship.compose.ui.theme.Accent2_800
import com.mentorship.compose.ui.theme.Accent800
import com.mentorship.compose.ui.theme.ComposeTheme
import com.mentorship.compose.ui.theme.Neutral100
import com.mentorship.compose.ui.theme.Neutral800
import com.mentorship.compose.ui.theme.PillShape

@Composable
fun CustomChip(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color,
    text: String,
    border: BorderStroke? = null
) {
    Surface(
        modifier = modifier,
        shape = PillShape,
        color = containerColor,
        contentColor = contentColor,
        border = border,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            text = text,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Composable
fun StatusChip(modifier: Modifier = Modifier, status: Status) {
    CustomChip(
        modifier = modifier,
        containerColor = status.containerColor,
        contentColor = status.contentColor,
        text = status.text
    )
}

@Composable
fun PriorityChip(modifier: Modifier = Modifier, priority: Priority) {
    CustomChip(
        modifier = modifier,
        containerColor = priority.containerColor,
        contentColor = priority.contentColor,
        text = priority.text
    )
}

enum class Status(val containerColor: Color, val contentColor: Color, val text: String) {
    ToDo(Neutral100, Neutral800, "To Do"),
    InProgress(Accent100, Accent800, "In Progress"),
    Done(Accent2_100, Accent2_800, "Done"),
}

enum class Priority(val containerColor: Color, val contentColor: Color, val text: String) {
    Low(Accent2_100, Accent2_800, "Low priority"),
    Medium(Neutral100, Neutral800, "Medium priority"),
    High(Accent100, Accent800, "High priority"),
}

@Preview
@Composable
private fun CustomChipPreview() {
    ComposeTheme {
        CustomChip(
            containerColor = Accent2_100,
            contentColor = Accent2_800,
            text = "test chippo"
        )
    }
}