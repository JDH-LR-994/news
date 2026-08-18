package com.likhodievskii.news.domain.mapper

import com.likhodievskii.news.domain.entity.Interval
import com.likhodievskii.news.domain.entity.Language
import com.likhodievskii.news.domain.entity.RefreshConfig
import com.likhodievskii.news.domain.entity.Settings

fun Int.toInterval(): Interval {
    return Interval.entries.first { it.minutes == this }
}

fun String.toLanguage(): Language {
    return Language.valueOf(this)
}

fun Settings.toRefreshConfig(): RefreshConfig {
    return RefreshConfig(
        language = language,
        interval = interval,
        wifiOnly = wifiOnly
    )
}