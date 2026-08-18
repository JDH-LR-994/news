package com.likhodievskii.news.data.local

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "subscriptions")
data class SubscriptionDbModel(
    @PrimaryKey
    val topic: String
)
