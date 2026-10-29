package com.mentorship.compose.di

import com.mentorship.compose.data.repository.ChatRepository
import com.mentorship.compose.data.repository.ChatRepositoryImpl
import com.mentorship.compose.data.repository.TaskRepository
import com.mentorship.compose.data.repository.TaskRepositoryImpl
import com.mentorship.compose.data.repository.UserRepository
import com.mentorship.compose.data.repository.UserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun tasks(implementation: TaskRepositoryImpl): TaskRepository

    @Binds
    abstract fun users(implementation: UserRepositoryImpl): UserRepository

    @Binds
    abstract fun chat(implementation: ChatRepositoryImpl): ChatRepository
}