package com.dtprogramming.treasurehuntirl.util

import android.database.Cursor
import java.util.*

/**
 * Created by ryantaylor on 6/29/16.
 */

fun randomUuid(): String {
    return UUID.randomUUID().toString().replace("-", "")
}

fun Cursor.getString(columnName: String): String {
    return getString(getColumnIndex(columnName))
}

fun Cursor.getDouble(columnName: String): Double {
    return getDouble(getColumnIndex(columnName))
}

fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)
