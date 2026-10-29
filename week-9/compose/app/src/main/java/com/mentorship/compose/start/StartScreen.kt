@file:OptIn(ExperimentalMaterial3Api::class)

package com.mentorship.compose.start

import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mentorship.compose.ui.theme.ComposeTheme

@Composable
fun StartScreen(onChatClick: () -> Unit, onTasksClick: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Start",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(it)
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(16.dp)
        ) {

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onChatClick
            ) {
                Text(
                    text = "Chat",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = onTasksClick
            ) {
                Text(
                    text = "Tasks",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                )
            }
        }
    }
}

@Preview
@Composable
private fun StartScreenPreview() {
    ComposeTheme {
        StartScreen({}, {})
    }
}