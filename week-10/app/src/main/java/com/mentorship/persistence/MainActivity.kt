package com.mentorship.persistence

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mentorship.persistence.ui.theme.PersistenceTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val users by viewModel.users.collectAsStateWithLifecycle()
            val tasks by viewModel.tasks.collectAsStateWithLifecycle()
            val mediaFiles by viewModel.mediaFiles.collectAsStateWithLifecycle()
            val feedPosts by viewModel.feedPosts.collectAsStateWithLifecycle()
            val usersWithCreatedTasks by viewModel.usersWithCreatedTasks.collectAsStateWithLifecycle()
            val usersWithAssignedTasks by viewModel.usersWithAssignedTasks.collectAsStateWithLifecycle()
            val tasksWithAssignedUsers by viewModel.tasksWithAssignedUsers.collectAsStateWithLifecycle()
            val tasksWithMediaFiles by viewModel.tasksWithMediaFiles.collectAsStateWithLifecycle()
            val feedPostsWithMediaFiles by viewModel.feedPostsWithMediaFiles.collectAsStateWithLifecycle()
            LaunchedEffect(Unit) { viewModel.seedDatabase() }
            PersistenceTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = innerPadding
                    ) {
                        item(key = "import") {
                            GetContentExample(viewModel::onLoadDocument)
                        }
                        item(key = "users") {
                            DbHideableColumn("Users", users) { DbEntry(it) }
                        }
                        item(key = "tasks") {
                            DbHideableColumn("Tasks", tasks) { DbEntry(it) }
                        }
                        item(key = "mediaFiles") {
                            DbHideableColumn("Media files", mediaFiles) { DbEntry(it) }
                        }
                        item(key = "feedPosts") {
                            DbHideableColumn("Feed posts", feedPosts) { DbEntry(it) }
                        }
                        item(key = "usersWithCreatedTasks") {
                            DbHideableColumn(
                                "Users with created tasks",
                                usersWithCreatedTasks
                            ) { DbEntry(it) }
                        }
                        item(key = "usersWithAssignedTasks") {
                            DbHideableColumn(
                                "Users with assigned tasks",
                                usersWithAssignedTasks
                            ) { DbEntry(it) }
                        }
                        item(key = "tasksWithAssignedUsers") {
                            DbHideableColumn(
                                "Tasks with assigned users",
                                tasksWithAssignedUsers
                            ) { DbEntry(it) }
                        }
                        item(key = "tasksWithMediaFiles") {
                            DbHideableColumn(
                                "Tasks with media files",
                                tasksWithMediaFiles
                            ) { DbEntry(it) }
                        }
                        item(key = "feedPostsWithMediaFiles") {
                            DbHideableColumn(
                                "Feed posts with media files",
                                feedPostsWithMediaFiles
                            ) { DbEntry(it) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun <T> DbHideableColumn(
    title: String,
    entries: List<T>,
    entryContent: @Composable (T) -> Unit
) {
    var visible by rememberSaveable { mutableStateOf(false) }
    Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Button(onClick = { visible = !visible }) {
            Text("${if (visible) "Hide" else "Show"} $title (${entries.size})")
        }
        if (visible) {
            if (entries.isEmpty()) {
                Text("No rows")
            }
            entries.forEach { entry ->
                Column(Modifier.padding(vertical = 8.dp)) {
                    entryContent(entry)
                }
                HorizontalDivider()
            }
        }
    }
}

@Composable
private fun DbField(label: String, value: Any?) {
    Text(buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
            append("$label: ")
        }
        append(value.toString())
    })
}

@Composable
fun DbEntry(entry: UserEntity) {
    DbField("user.id", entry.id)
    DbField("user.name", entry.name)
    DbField("user.occupation", entry.occupation)
}

@Composable
fun DbEntry(entry: TaskEntity) {
    DbField("task.id", entry.id)
    DbField("task.name", entry.name)
    DbField("task.deadline", entry.deadline)
    DbField("task.createdById", entry.createdById)
}

@Composable
fun DbEntry(entry: MediaFileEntity) {
    DbField("mediaFile.id", entry.id)
    DbField("mediaFile.name", entry.name)
    DbField("mediaFile.fullPathInStorage", entry.fullPathInStorage)
    DbField("mediaFile.type", entry.type)
}

@Composable
fun DbEntry(entry: FeedPostEntity) {
    DbField("feedPost.id", entry.id)
    DbField("feedPost.title", entry.title)
    DbField("feedPost.description", entry.description)
}

@Composable
fun DbEntry(entry: UserWithCreatedTasks) {
    DbEntry(entry.user)
    DbField("Created tasks", entry.tasks.size)
    entry.tasks.forEach { child ->
        Column(Modifier.padding(start = 16.dp, top = 8.dp)) {
            DbEntry(child)
        }
    }
}

@Composable
fun DbEntry(entry: UserWithAssignedTasks) {
    DbEntry(entry.user)
    DbField("Assigned tasks", entry.task.size)
    entry.task.forEach { child ->
        Column(Modifier.padding(start = 16.dp, top = 8.dp)) {
            DbEntry(child)
        }
    }
}

@Composable
fun DbEntry(entry: TaskWithAssignedUsers) {
    DbEntry(entry.task)
    DbField("Assigned users", entry.user.size)
    entry.user.forEach { child ->
        Column(Modifier.padding(start = 16.dp, top = 8.dp)) {
            DbEntry(child)
        }
    }
}

@Composable
fun DbEntry(entry: TaskWithMediaFiles) {
    DbEntry(entry.task)
    DbField("Media files", entry.mediaFile.size)
    entry.mediaFile.forEach { child ->
        Column(Modifier.padding(start = 16.dp, top = 8.dp)) {
            DbEntry(child)
        }
    }
}

@Composable
fun DbEntry(entry: FeedPostWithMediaFiles) {
    DbEntry(entry.feedPostEntity)
    DbField("Media files", entry.mediaFile.size)
    entry.mediaFile.forEach { child ->
        Column(Modifier.padding(start = 16.dp, top = 8.dp)) {
            DbEntry(child)
        }
    }
}

@Composable
fun GetContentExample(onLoadDocument: (Uri?) -> Unit) {
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument(),
        onLoadDocument
    )
    Button(
        onClick = { launcher.launch(arrayOf("*/*")) },
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text("Load Documento")
    }
}
