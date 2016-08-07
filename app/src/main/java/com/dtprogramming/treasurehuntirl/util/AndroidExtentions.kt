package com.dtprogramming.treasurehuntirl.util

import android.database.Cursor
import android.view.View
import java.util.*

/**
 * Created by ryantaylor on 6/29/16.
 */

// UUID
fun randomUuid(): String {
    return UUID.randomUUID().toString().replace("-", "")
}

// Cursor

fun Cursor.getString(columnName: String): String {
    return getString(getColumnIndex(columnName))
}

fun Cursor.getStringOrNull(columnName: String): String? {
    if (getColumnIndex(columnName) == -1)
        return null
    else
        return getString(getColumnIndex(columnName))
}

fun Cursor.getDouble(columnName: String): Double {
    return getDouble(getColumnIndex(columnName))
}

fun Cursor.getLong(columnName: String): Long {
    return getLong(getColumnIndex(columnName))
}

fun Cursor.getInt(columnName: String): Int {
    return getInt(getColumnIndex(columnName))
}

fun Cursor.getBoolean(columnName: String): Boolean {
    return getInt(getColumnIndex(columnName)) == 1
}

// Double
fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)

// View
fun View.gone() { visibility = View.GONE}

fun View.visible() { visibility = View.VISIBLE }
