package com.dtprogramming.treasurehuntirl.database

import android.database.Cursor
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by ryantaylor on 6/22/16.
 */
object DatabaseObservables {

    fun getTreasureHunt(uuid: String): Observable<MutableList<TreasureHunt>> {
        return THApp.briteDatabase.createQuery(TreasureHunt.TABLE.NAME, TableColumns.WHERE_UUID_EQUALS, uuid)
                .mapToList { TreasureHunt(it.getString(TableColumns.UUID), it.getString(TreasureHunt.TABLE.TITLE)) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getTreasureHunts(): Observable<MutableList<TreasureHunt>> {
        return THApp.briteDatabase.createQuery(TreasureHunt.TABLE.NAME, "SELECT * FROM ${TreasureHunt.TABLE.NAME}")
                .mapToList { TreasureHunt(it.getString(TableColumns.UUID), it.getString(TreasureHunt.TABLE.TITLE)) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getClueObservable(treasureHuntId: String): Observable<MutableList<Clue>> {
        return THApp.briteDatabase.createQuery(Clue.TABLE.NAME, "SELECT * FROM ${Clue.TABLE.NAME} WHERE ${Clue.TABLE.TREASURE_HUNT}=?", treasureHuntId)
                .mapToList { Clue(it.getString(TableColumns.UUID), it.getString(Clue.TABLE.TREASURE_HUNT), it.getString(Clue.TABLE.TEXT)) }
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getWaypointObservable(treasureHuntId: String): Observable<MutableList<Waypoint>> {
        return THApp.briteDatabase.createQuery(Waypoint.TABLE.NAME, "SELECT * FROM ${Waypoint.TABLE.NAME} WHERE ${Waypoint.TABLE.TREASURE_HUNT}=?", treasureHuntId)
                .mapToList { Waypoint(it.getString(TableColumns.UUID), it.getString(Waypoint.TABLE.TITLE), it.getString(Waypoint.TABLE.TREASURE_HUNT), it.getDouble(Waypoint.TABLE.LAT), it.getDouble(Waypoint.TABLE.LNG)) }
                .observeOn(AndroidSchedulers.mainThread())
    }
}

fun Cursor.getString(columnName: String): String {
    return getString(getColumnIndex(columnName))
}

fun Cursor.getDouble(columnName: String): Double {
    return getDouble(getColumnIndex(columnName))
}
