package com.dtprogramming.treasurehuntirl

import android.app.Application
import com.dtprogramming.treasurehuntirl.database.DatabaseOpenHelper
import com.dtprogramming.treasurehuntirl.injection.DaggerDatabaseComponent
import com.dtprogramming.treasurehuntirl.injection.DatabaseComponent
import com.dtprogramming.treasurehuntirl.injection.DatabaseModule
import com.squareup.leakcanary.LeakCanary
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import rx.schedulers.Schedulers

/**
 * Created by ryantaylor on 6/15/16.
 */
class THApp : Application() {

    companion object {
        lateinit var databaseComponent: DatabaseComponent
    }

    override fun onCreate() {
        super.onCreate()

        LeakCanary.install(this)

        databaseComponent= DaggerDatabaseComponent.builder().databaseModule(DatabaseModule(baseContext)).build()
    }
}