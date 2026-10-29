package com.mentorship.compose.ui.components

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mentorship.compose.ui.theme.ComposeTheme

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        onClick = { onClick?.invoke() },
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = spacedBy(8.dp),
            content = content,
        )
    }
}

@Preview
@Composable
private fun CustomCardPreview() {
    ComposeTheme {
        CustomCard {
            Text("Shalom")
        }
    }
}