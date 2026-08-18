package com.likhodievskii.news.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.content.getSystemService
import androidx.room3.Room
import androidx.work.WorkManager
import com.likhodievskii.news.data.local.NewsDatabase
import com.likhodievskii.news.data.remote.NewsApiService
import com.likhodievskii.news.data.repository.NewsRepositoryImpl
import com.likhodievskii.news.data.repository.SettingsRepositoryImpl
import com.likhodievskii.news.domain.repository.NewsRepository
import com.likhodievskii.news.domain.repository.SettingsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    @Singleton
    fun bindNewsRepository(
        impl: NewsRepositoryImpl
    ): NewsRepository

    @Binds
    @Singleton
    fun bindSettingsRepository(
        impl: SettingsRepositoryImpl
    ): SettingsRepository

    companion object {
        @Provides
        @Singleton
        fun provideJson() = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        @Provides
        @Singleton
        fun provideNotificationManager(
            @ApplicationContext context: Context
        ) = context.getSystemService<NotificationManager>()


        @Provides
        @Singleton
        fun provideConverterFactory(
            json: Json
        ) = json.asConverterFactory(
            "application/json".toMediaType()
        )

        @Provides
        @Singleton
        fun provideWorkManager(
            @ApplicationContext context: Context
        ): WorkManager = WorkManager.getInstance(context)

        @Provides
        @Singleton
        fun provideRetrofit(
            converterFactory: Converter.Factory
        ): Retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/")
            .addConverterFactory(converterFactory)
            .build()




        @Provides
        @Singleton
        fun provideApiService(
            retrofit: Retrofit
        ): NewsApiService {
            return retrofit.create()
        }

        @Provides
        @Singleton
        fun provideNewsDatabase(
            @ApplicationContext context: Context
        ) = Room.databaseBuilder(
            context = context,
            klass = NewsDatabase::class.java,
            name = "news.db"
        ).fallbackToDestructiveMigration(true).build()


        @Provides
        @Singleton
        fun provideNewsDao(
            newsDatabase: NewsDatabase
        ) = newsDatabase.getNewsDao()
    }
}
