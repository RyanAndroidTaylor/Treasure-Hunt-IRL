package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.Answer

/**
 * Created by ryantaylor on 7/7/16.
 */
interface AnswerConnection : Connection {

    fun insert(answer: Answer)
    fun update(answer: Answer)
}