@file:OptIn(ExperimentalMaterial3Api::class)

package com.mentorship.compose.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Message
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.ArrowForwardIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mentorship.compose.domain.entity.Chat
import com.mentorship.compose.ui.components.CustomCard
import com.mentorship.compose.ui.components.CustomChip
import com.mentorship.compose.ui.theme.Accent2_100
import com.mentorship.compose.ui.theme.Accent2_800
import com.mentorship.compose.ui.theme.ComposeTheme
import com.mentorship.compose.ui.theme.Neutral100
import com.mentorship.compose.ui.theme.Neutral800
import com.mentorship.compose.ui.theme.PillShape

@Composable
fun ChatDetailsScreen(
    viewModel: ChatDetailsViewModel = hiltViewModel(),
    onParticipantsClick: () -> Unit,
    onBack: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ChatDetailsScreen(
        state = state,
        onParticipantsClick = onParticipantsClick,
        onBack = onBack
    )
}

@Composable
private fun ChatDetailsScreen(state: Chat, onParticipantsClick: () -> Unit, onBack: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chat Details",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                            contentDescription = null
                        )
                    }
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
            Surface(shape = PillShape, color = Accent2_100) {
                Icon(
                    modifier = Modifier.padding(12.dp),
                    imageVector = Icons.AutoMirrored.Outlined.Message,
                    contentDescription = null
                )
            }
            Text(
                text = state.name,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Row(horizontalArrangement = spacedBy(12.dp)) {
                CustomChip(
                    text = "Group chat",
                    containerColor = Accent2_100,
                    contentColor = Accent2_800
                )
                CustomChip(
                    text = "Active ${state.lastActivity}",
                    containerColor = Neutral100,
                    contentColor = Neutral800
                )
            }
            Spacer(Modifier.height(8.dp))
            CustomCard(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "About",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = state.description,
                    style = MaterialTheme.typography.labelMedium,
                )
            }
            CustomCard(
                modifier = Modifier.fillMaxWidth(),
                onClick = onParticipantsClick,
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = "Participants",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "${state.participants.size} members",
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = spacedBy((-12).dp)
                    ) {
                        state.participants.take(3).forEach { participant ->
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(PillShape)
                                    .background(Accent2_100)
                                    .border(2.dp, MaterialTheme.colorScheme.surface, PillShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = participant.initials,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                        Spacer(Modifier.width(32.dp))
                        Icon(
                            modifier = Modifier.size(16.dp),
                            imageVector = Icons.AutoMirrored.Rounded.ArrowForwardIos,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ChatDetailsScreenPreview() {
    ComposeTheme {
        ChatDetailsScreen(
            state = Chat.mock(),
            onParticipantsClick = {},
            onBack = {}
        )
    }
}