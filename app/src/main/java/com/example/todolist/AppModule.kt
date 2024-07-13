package com.example.todolist

import android.content.Context
import com.example.todolist.data.database.DatabaseSource
import com.example.todolist.data.database.TodoDao
import com.example.todolist.data.network.api.ToDoApi
import com.example.todolist.data.network.datasource.NetworkDatasource
import com.example.todolist.data.repository.NetworkRepository
import com.example.todolist.data.repository.RemoteRepository
import com.example.todolist.data.repository.SettingRepository
import com.example.todolist.domain.IDatabaseSource
import com.example.todolist.domain.INetworkDatasource
import com.example.todolist.domain.repository.ISettingRepository
import com.example.todolist.domain.repository.ITaskLocalRepository
import com.example.todolist.domain.repository.ITaskRepository
import com.example.todolist.errorHandling.ErrorHandlingImpl
import com.example.todolist.errorHandling.IErrorHandling
import com.example.todolist.providers.IStringProvider
import com.example.todolist.providers.StringProviderImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNetworkDataSource(api: ToDoApi): INetworkDatasource {
        return NetworkDatasource(api)
    }

    @Provides
    @Singleton
    fun provideDatabaseSource(dao: TodoDao): IDatabaseSource {
        return DatabaseSource(dao)
    }


    /*
        @Provides
        @Singleton
        fun provideTaskRepository(
            networkDataSource: INetworkDatasource,
            settingRepository: ISettingRepository,
            databaseSource: IDatabaseSource,
        ): ITaskRepository {
            return TaskRepository(networkDataSource, settingRepository, databaseSource)
        }

     */


    @Provides
    @Singleton
    fun provideRemoteRepository(
        databaseSource: IDatabaseSource,
        repository: ITaskRepository,
        stringProvider: IStringProvider
    ): ITaskLocalRepository {
        return RemoteRepository(databaseSource, repository, stringProvider)
    }

    @Provides
    @Singleton
    fun provideNetworkRepository(
        networkDataSource: INetworkDatasource,
        settingRepository: ISettingRepository,
        databaseSource: IDatabaseSource,
        errorHandling: IErrorHandling,
        stringProvider: IStringProvider
    ): ITaskRepository {
        return NetworkRepository(
            networkDataSource,
            settingRepository,
            databaseSource,
            errorHandling,
            stringProvider
        )
    }

    @Provides
    @Singleton
    fun provideSettingRepository(@ApplicationContext context: Context): ISettingRepository {
        return SettingRepository(context)
    }

    @Provides
    @Singleton
    fun provideStringProviderImpl(@ApplicationContext context: Context): IStringProvider {
        return StringProviderImpl(context)
    }

    @Provides
    @Singleton
    fun provideErrorHandlingImpl(@ApplicationContext context: Context): IErrorHandling {
        return ErrorHandlingImpl(context)
    }

}

