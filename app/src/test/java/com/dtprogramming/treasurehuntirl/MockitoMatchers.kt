package com.dtprogramming.treasurehuntirl

import org.mockito.Mockito

/**
 * Created by ryantaylor on 6/26/16.
 */
object MockitoMatchers {

    fun <T> anyObject(): T {
        Mockito.anyObject<T>()

        return null as T
    }
}