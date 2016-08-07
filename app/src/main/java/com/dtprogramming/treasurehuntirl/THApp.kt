package com.dtprogramming.treasurehuntirl

import android.app.Application
import com.dtprogramming.treasurehuntirl.database.DatabaseOpenHelper
import com.squareup.leakcanary.LeakCanary
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import rx.schedulers.Schedulers

/**
 * Created by ryantaylor on 6/15/16.
 */
class THApp : Application() {

    companion object {
        lateinit var briteDatabase: BriteDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        LeakCanary.install(this)

        val database = DatabaseOpenHelper(baseContext)
        briteDatabase = SqlBrite.create().wrapDatabaseHelper(database, Schedulers.io())
    }
}