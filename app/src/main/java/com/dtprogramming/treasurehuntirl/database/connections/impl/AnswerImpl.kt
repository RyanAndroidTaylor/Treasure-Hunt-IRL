package com.dtprogramming.treasurehuntirl.database.connections.impl

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.AnswerConnection
import com.dtprogramming.treasurehuntirl.database.models.Answer
import com.squareup.sqlbrite.BriteDatabase
import rx.Subscription
import java.util.*

/**
 * Created by ryantaylor on 7/7/16.
 */
class AnswerImpl : AnswerConnection {

    override val connections = ArrayList<Subscription>()

    private val database: BriteDatabase

    init {
        database = THApp.briteDatabase
    }

    override fun insert(answer: Answer) {
        database.insert(Answer.TABLE.NAME, answer.getContentValues())
    }

    override fun update(answer: Answer) {
        database.update(Answer.TABLE.NAME, answer.getContentValues(), TableColumns.WHERE_UUID_EQUALS, answer.uuid)
    }

    override fun unsubscribe() {
        for (connection in connections)
            if (!connection.isUnsubscribed)
                connection.unsubscribe()
    }
}