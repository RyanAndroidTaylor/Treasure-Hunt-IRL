package com.dtprogramming.treasurehuntirl.util

import java.util.*

/**
 * Created by ryantaylor on 6/29/16.
 */

fun randomUuid(): String {
    return UUID.randomUUID().toString().replace("-", "")
}
