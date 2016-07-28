package com.dtprogramming.treasurehuntirl.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteCursorDriver
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQuery
import com.dtprogramming.treasurehuntirl.database.models.*

/**
 * Created by ryantaylor on 6/21/16.
 */
class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, "THApp", null, 1) {

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(TreasureHunt.TABLE.CREATE)
        db?.execSQL(TextClue.TABLE.CREATE)
        db?.execSQL(Waypoint.TABLE.CREATE)
        db?.execSQL(TreasureChest.TABLE.CREATE)

        db?.execSQL(PlayingTreasureHunt.TABLE.CREATE)
        db?.execSQL(CollectedTreasureChest.TABLE.CREATE)
        db?.execSQL(CollectedTextClue.TABLE.CREATE)
    }
}