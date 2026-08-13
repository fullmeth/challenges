@file:OptIn(ExperimentalMaterial3Api::class)

package com.mentorship.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.mentorship.project.ui.theme.ProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectTheme {
                HomeScreen()
            }
        }
    }
}

@Composable
fun HomeScreen() {
    val context = LocalContext.current.applicationContext
    val repository = remember { RepositoryImpl(context) }
    val viewModel = viewModel<MainViewModel>(factory = viewModelFactory {
        initializer {
            MainViewModel(repository)
        }
    })
    val snackbarHostState = remember { SnackbarHostState() }
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is UiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(message = event.message)
            }
        }
    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState, modifier = Modifier.imePadding()) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SafeApiCallTestComponent(onSendRequestClick = viewModel::getResponse)
            SharedPreferencesTestComponent(
                state = state,
                onGetValueClick = viewModel::getPreference,
                onSetValueClick = viewModel::savePreference,
                onSelectType = viewModel::selectSharedPreferenceType,
            )
        }
    }
}

@Composable
fun SafeApiCallTestComponent(onSendRequestClick: (Int, Int?) -> Unit) {
    var safeApiCallCardState by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        onClick = { safeApiCallCardState = !safeApiCallCardState }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(12.dp)
        ) {
            val options = listOf(
                "200", "400", "401", "403", "404", "408", "429", "500", "502", "503"
            )
            var dropdownState by remember { mutableStateOf(false) }
            val dropdownTextFieldState = rememberTextFieldState(options.first())
            var checkedState by remember { mutableStateOf(false) }
            var delayTextFieldState by remember { mutableStateOf("") }
            Text(text = "safeApiCall test suite")
            if (safeApiCallCardState) {
                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = dropdownState,
                    onExpandedChange = { dropdownState = it }
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        readOnly = true,
                        state = dropdownTextFieldState,
                        label = { Text("Response code") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownState) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    )
                    ExposedDropdownMenu(
                        expanded = dropdownState,
                        onDismissRequest = { dropdownState = false }
                    ) {
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    dropdownTextFieldState.setTextAndPlaceCursorAtEnd(
                                        selectionOption
                                    )
                                    dropdownState = false
                                },
                                text = { Text(text = selectionOption) }
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { checkedState = !checkedState },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "with delay?")
                    Checkbox(
                        checked = checkedState,
                        onCheckedChange = { checkedState = it }
                    )
                }
                if (checkedState) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Delay") },
                        value = delayTextFieldState,
                        onValueChange = { delayTextFieldState = it },
                        keyboardActions = KeyboardActions(onAny = {
                            onSendRequestClick(
                                dropdownTextFieldState.text.toString().toIntOrNull() ?: 400,
                                delayTextFieldState.toIntOrNull()
                            )
                        }),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        )
                    )
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onSendRequestClick(
                            dropdownTextFieldState.text.toString().toIntOrNull() ?: 400,
                            delayTextFieldState.toIntOrNull()
                        )
                    }
                ) { Text(text = "Send request!") }
            }
        }
    }
}

@Composable
fun SharedPreferencesTestComponent(
    state: MainUiState,
    onGetValueClick: () -> Unit,
    onSetValueClick: (String) -> Unit,
    onSelectType: (SharedPreferenceType) -> Unit,
) {
    var sharedPreferencesCardState by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        onClick = { sharedPreferencesCardState = !sharedPreferencesCardState }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .animateContentSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = spacedBy(12.dp)
        ) {
            Text(text = "PreferenceDelegate test suite")
            if (sharedPreferencesCardState) {
                var textFieldState by remember { mutableStateOf("") }
                val options = SharedPreferenceType.entries
                var dropdownState by remember { mutableStateOf(false) }
                val dropdownTextFieldState =
                    rememberTextFieldState(options.first().name.lowercase())
                ExposedDropdownMenuBox(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = dropdownState,
                    onExpandedChange = { dropdownState = it }
                ) {
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
                        readOnly = true,
                        state = dropdownTextFieldState,
                        label = { Text("SharedPreference Type") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = dropdownState) },
                        colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    )
                    ExposedDropdownMenu(
                        expanded = dropdownState,
                        onDismissRequest = { dropdownState = false }
                    ) {
                        options.forEach { selectionOption ->
                            DropdownMenuItem(
                                onClick = {
                                    dropdownTextFieldState.setTextAndPlaceCursorAtEnd(
                                        selectionOption.name.lowercase()
                                    )
                                    onSelectType(selectionOption)

                                    dropdownState = false
                                },
                                text = {
                                    Text(
                                        text = selectionOption.name.lowercase()
                                    )
                                }
                            )
                        }
                    }
                }
                Text(text = "SharedPreference value:")
                Text(text = "${state.sharedPreferenceValue} (${state.selectedPreferenceType.name.lowercase()})")
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = state.selectedPreferenceType.name
                                .lowercase()
                                .capitalize(LocalLocale.current)
                        )
                    },
                    value = textFieldState,
                    onValueChange = { textFieldState = it },
                    keyboardActions = KeyboardActions(onAny = { onSetValueClick(textFieldState) }),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = when (state.selectedPreferenceType) {
                            SharedPreferenceType.INT,
                            SharedPreferenceType.LONG,
                            SharedPreferenceType.FLOAT -> KeyboardType.Number

                            else -> KeyboardType.Text
                        },
                        imeAction = ImeAction.Done
                    )
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = spacedBy(12.dp),
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = onGetValueClick
                    ) { Text(text = "Get") }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { onSetValueClick(textFieldState) }
                    ) { Text(text = "Set") }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrefsTestPreview() {
    ProjectTheme {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxSize(),
            verticalArrangement = spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SafeApiCallTestComponent { _, _ -> }
            SharedPreferencesTestComponent(
                state = MainUiState.initial(),
                onGetValueClick = {},
                onSetValueClick = {},
                onSelectType = {}
            )
        }
    }
}