package com.mentorship.compose.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.dropUnlessResumed
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.mentorship.compose.chat.ChatDetailsScreen
import com.mentorship.compose.start.StartScreen
import com.mentorship.compose.tasks.ChangeTaskStatusScreen
import com.mentorship.compose.tasks.ChangeTaskStatusViewModel
import com.mentorship.compose.tasks.TaskDetailsScreen
import com.mentorship.compose.tasks.TaskDetailsViewModel
import com.mentorship.compose.tasks.TasksListScreen
import com.mentorship.compose.tasks.TasksListViewModel
import com.mentorship.compose.tasks.filters.FiltersScreen
import com.mentorship.compose.users.UserListScreen
import com.mentorship.compose.users.UsersListViewModel

@Composable
fun AppNavigation() {
    val backStack = remember { mutableStateListOf<Any>(StartRoute) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberSharedViewModelStoreNavEntryDecorator(),
        ),
        entryProvider = entryProvider {
            entry<StartRoute> {
                StartScreen(
                    onChatClick = dropUnlessResumed { backStack.add(ChatDetailsRoute) },
                    onTasksClick = dropUnlessResumed { backStack.add(TasksListRoute) },
                )
            }
            entry<ChatDetailsRoute> {
                ChatDetailsScreen(
                    onParticipantsClick = dropUnlessResumed {
                        backStack.add(
                            UsersListRoute(UsersSource.Chat(-1))
                        )
                    },
                    onBack = dropUnlessResumed { backStack.removeLastOrNull() })
            }
            entry<UsersListRoute> { key ->
                val viewModel =
                    hiltViewModel<UsersListViewModel, UsersListViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key)
                        }
                    )
                UserListScreen(
                    viewModel = viewModel,
                    onBack = dropUnlessResumed { backStack.removeLastOrNull() }
                )
            }
            entry<TasksListRoute>(
                clazzContentKey = { it.toContentKey() }
            ) {
                TasksListScreen(
                    onTaskClick = { id -> backStack.add(TaskRoute(id)) },
                    onFilterClick = dropUnlessResumed { backStack.add(FilterRoute) },
                    onBack = dropUnlessResumed { backStack.removeLastOrNull() }
                )
            }
            entry<TaskRoute> { key ->
                val viewModel =
                    hiltViewModel<TaskDetailsViewModel, TaskDetailsViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key)
                        }
                    )
                TaskDetailsScreen(
                    viewModel = viewModel,
                    onChangeTaskStatus = { id ->
                        backStack.add(
                            ChangeTaskStatusRoute(id)
                        )
                    },
                    onParticipantsClick = { id ->
                        backStack.add(
                            UsersListRoute(UsersSource.Task(id))
                        )
                    },
                    onBack = dropUnlessResumed { backStack.removeLastOrNull() }
                )
            }
            entry<FilterRoute>(
                metadata = SharedViewModelStoreNavEntryDecorator.parent(
                    TasksListRoute.toContentKey()
                )
            ) {
                val parentViewModel = hiltViewModel<TasksListViewModel>(
                    viewModelStoreOwner = LocalSharedViewModelStoreOwner.current
                )
                FiltersScreen(
                    viewModel = parentViewModel,
                    onBack = dropUnlessResumed { backStack.removeLastOrNull() }
                )
            }
            entry<ChangeTaskStatusRoute> { key ->
                val viewModel =
                    hiltViewModel<ChangeTaskStatusViewModel, ChangeTaskStatusViewModel.Factory>(
                        creationCallback = { factory ->
                            factory.create(key)
                        }
                    )
                ChangeTaskStatusScreen(
                    viewModel = viewModel,
                    onBack = dropUnlessResumed { backStack.removeLastOrNull() }
                )
            }
        }
    )
}