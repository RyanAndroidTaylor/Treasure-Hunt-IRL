package com.dtprogramming.treasurehuntirl

import android.app.Application
import com.dtprogramming.treasurehuntirl.database.DatabaseOpenHelper
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import rx.schedulers.Schedulers

/**
 * Created by ryantaylor on 6/15/16.
 */
class THApp : Application() {

    val test = ""

    companion object {
        lateinit var briteDatabase: BriteDatabase
            private set
    }

    init {

    }

    override fun onCreate() {
        super.onCreate()

        briteDatabase = SqlBrite.create().wrapDatabaseHelper(DatabaseOpenHelper(baseContext), Schedulers.io())
    }
}