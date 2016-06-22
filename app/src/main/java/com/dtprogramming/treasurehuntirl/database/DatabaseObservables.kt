package com.dtprogramming.treasurehuntirl.database

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.models.Clue
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by ryantaylor on 6/22/16.
 */
object DatabaseObservables {

    fun getClueObservable(treasureHuntId: String): Observable<MutableList<Clue>> {
        return THApp.briteDatabase.createQuery(Clue.TABLE.NAME, "SELECT * FROM ${Clue.TABLE.NAME}")
                .mapToList { Clue(it.getString(it.getColumnIndex(TableColumns.UUID)), it.getString(it.getColumnIndex(Clue.TABLE.TREASURE_HUNT)), it.getString(it.getColumnIndex(Clue.TABLE.TEXT))) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }
}