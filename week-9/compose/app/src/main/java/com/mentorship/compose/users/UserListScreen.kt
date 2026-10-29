@file:OptIn(ExperimentalMaterial3Api::class)

package com.mentorship.compose.users

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mentorship.compose.data.entity.User
import com.mentorship.compose.ui.theme.Accent2_100
import com.mentorship.compose.ui.theme.ComposeTheme
import com.mentorship.compose.ui.theme.Divider
import com.mentorship.compose.ui.theme.PillShape

@Composable
fun UserListScreen(viewModel: UsersListViewModel, onBack: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    UserListScreen(state, onBack)
}

@Composable
private fun UserListScreen(state: List<User>, onBack: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Users",
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(12.dp),
            verticalArrangement = spacedBy(12.dp)
        ) {
            var searchQuery by remember { mutableStateOf("") }
            val filteredUserList = state.filter { user ->
                user.displayName.contains(searchQuery.trim(), ignoreCase = true)
            }
            Text(
                text = "Chat members",
                style = MaterialTheme.typography.bodySmall,
            )
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text(text = "Search people") },
                shape = PillShape,
            )
            LazyColumn(modifier = Modifier.fillMaxWidth(), verticalArrangement = spacedBy(8.dp)) {
                items(filteredUserList) { user ->
                    UserItem(
                        modifier = Modifier.fillMaxWidth(),
                        user = user
                    )
                }
            }
        }
    }
}

@Composable
fun UserItem(modifier: Modifier = Modifier, user: User) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(PillShape)
                .background(Accent2_100)
                .border(2.dp, MaterialTheme.colorScheme.surface, PillShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = user.initials,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Column(verticalArrangement = spacedBy(4.dp)) {
            Text(
                text = user.displayName,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = user.position,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
    Spacer(Modifier.height(8.dp))
    HorizontalDivider(modifier = Modifier.fillMaxWidth(), color = Divider)
}

@Preview
@Composable
private fun UserListScreenPreview() {
    ComposeTheme {
        UserListScreen(state = listOf(User("Alex", "Mercer", "Prototype")), {})
    }
}