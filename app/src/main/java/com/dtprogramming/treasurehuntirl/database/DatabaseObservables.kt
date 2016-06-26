package com.dtprogramming.treasurehuntirl.database

import android.database.Cursor
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by ryantaylor on 6/22/16.
 */
object DatabaseObservables {

    fun getClueObservable(treasureHuntId: String): Observable<MutableList<Clue>> {
        return THApp.briteDatabase.createQuery(Clue.TABLE.NAME, "SELECT * FROM ${Clue.TABLE.NAME}")
                .mapToList { Clue(it.getString(TableColumns.UUID), it.getString(Clue.TABLE.TREASURE_HUNT), it.getString(Clue.TABLE.TEXT)) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    fun getWaypointObservable(treasureHuntId: String): Observable<MutableList<Waypoint>> {
        return THApp.briteDatabase.createQuery(Waypoint.TABLE.NAME, "SELECT * FROM ${Waypoint.TABLE.NAME}")
        .mapToList { Waypoint(it.getString(TableColumns.UUID), it.getString(Waypoint.TABLE.TITLE), it.getString(Waypoint.TABLE.TREASURE_HUNT), it.getDouble(Waypoint.TABLE.LAT), it.getDouble(Waypoint.TABLE.LNG)) }
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
    }
}

fun Cursor.getString(columnName: String): String {
    return getString(getColumnIndex(columnName))
}

fun Cursor.getDouble(columnName: String): Double {
    return getDouble(getColumnIndex(columnName))
}
