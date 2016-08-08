package com.dtprogramming.treasurehuntirl.database.connections.impl

import android.database.sqlite.SQLiteDatabase
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TextClue
import com.dtprogramming.treasurehuntirl.database.models.CollectedTextClue
import com.dtprogramming.treasurehuntirl.util.getString
import com.squareup.sqlbrite.BriteDatabase
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*

/**
 * Created by ryantaylor on 7/5/16.
 */
class ClueConnectionImpl(override val database: BriteDatabase) : ClueConnection {

    override val subscriptions = ArrayList<Subscription>()

    override fun insert(textClue: TextClue) {
        database.insert(TextClue.TABLE.NAME, textClue.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE)
    }

    override fun update(textClue: TextClue) {
        database.update(TextClue.TABLE.NAME, textClue.getContentValues(), TableColumns.WHERE_UUID_EQUALS, textClue.uuid)
    }

    override fun getTextClue(textClueUuid: String): TextClue {
        val cursor = database.query("SELECT * FROM ${TextClue.TABLE.NAME} WHERE ${TableColumns.WHERE_UUID_EQUALS}", textClueUuid)

        cursor.moveToFirst()

        val clue = TextClue(cursor)

        cursor.close()

        return clue
    }

    override fun getTextClueForParent(parentUuid: String): TextClue? {
        val cursor = database.query("SELECT * FROM ${TextClue.TABLE.NAME} WHERE ${TextClue.TABLE.PARENT}=?", parentUuid)

        var clue: TextClue? = null

        if (cursor != null && cursor.moveToFirst())
            clue = TextClue(cursor)

        cursor.close()

        return clue
    }

    override fun getCluesForParentAsync(parentUuid: String, onComplete: (List<Clue>) -> Unit) {
        Observable.just(parentUuid)
        .map {
            val clues = ArrayList<Clue>()

            val textClues = getTextCluesForParent(parentUuid)

            textClues?.let {
                for (textClue in textClues)
                    clues.add(textClue)
            }

            clues
        }
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { onComplete(it) }
    }

    private fun getTextCluesForParent(parentUuid: String): List<TextClue>? {
        val cursor = database.query("SELECT * FROM ${TextClue.TABLE.NAME} WHERE ${TextClue.TABLE.PARENT}=?", parentUuid)

        if (cursor != null) {
            val textClues = ArrayList<TextClue>()

            while (cursor.moveToNext()) {
                textClues.add(TextClue(cursor))
            }

            cursor.close()

            return textClues
        }

        return null
    }
}