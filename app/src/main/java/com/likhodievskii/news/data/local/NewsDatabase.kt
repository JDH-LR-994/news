package com.likhodievskii.news.data.local

import androidx.room3.Database
import androidx.room3.RoomDatabase


@Database(
    entities = [ArticleDbModel::class, SubscriptionDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {

    abstract fun getNewsDao() : NewsDao

}