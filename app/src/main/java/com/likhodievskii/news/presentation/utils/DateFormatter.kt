package com.likhodievskii.news.presentation.utils

import java.text.DateFormat
import java.text.SimpleDateFormat

private val formatter = SimpleDateFormat.getDateInstance(DateFormat.SHORT)

fun Long.formatDate() = formatter.format(this)
