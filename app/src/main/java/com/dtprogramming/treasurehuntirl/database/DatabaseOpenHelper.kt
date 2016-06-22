package com.dtprogramming.treasurehuntirl.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt

/**
 * Created by ryantaylor on 6/21/16.
 */
class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(context, "THApp", null, 1) {

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(TreasureHunt.TABLE.CREATE)
        db?.execSQL(Clue.TABLE.CREATE)
    }
}