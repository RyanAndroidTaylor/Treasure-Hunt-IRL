package com.dtprogramming.treasurehuntirl.injection

import android.content.Context
import com.dtprogramming.treasurehuntirl.database.DatabaseOpenHelper
import com.dtprogramming.treasurehuntirl.database.connections.*
import com.dtprogramming.treasurehuntirl.database.connections.impl.*
import com.squareup.sqlbrite.BriteDatabase
import com.squareup.sqlbrite.SqlBrite
import dagger.Module
import dagger.Provides
import rx.schedulers.Schedulers
import javax.inject.Singleton

/**
 * Created by ryantaylor on 8/7/16.
 */
@Module
class DatabaseModule(context: Context) {

    private val briteDatabase: BriteDatabase

    init {
        val database = DatabaseOpenHelper(context)
        briteDatabase = SqlBrite.create().wrapDatabaseHelper(database, Schedulers.io())
    }

    @Provides
    @Singleton
    fun provideBriteDatabase(): BriteDatabase {
        return briteDatabase
    }

    @Provides
    @Singleton
    fun provideClueConnection(briteDatabase: BriteDatabase): ClueConnection {
        return ClueConnectionImpl(briteDatabase)
    }

    @Provides
    @Singleton
    fun provideCollectedClueConnection(briteDatabase: BriteDatabase): CollectedClueConnection {
        return CollectedClueConnectionImpl(briteDatabase)
    }

    @Provides
    @Singleton
    fun provideCollectedTreasureChestConnection(briteDatabase: BriteDatabase): CollectedTreasureChestConnection {
        return CollectedTreasureChestConnectionImpl(briteDatabase)
    }

    @Provides
    @Singleton
    fun provideInventoryConnection(briteDatabase: BriteDatabase): InventoryConnection {
        return InventoryConnectionImpl(briteDatabase)
    }

    @Provides
    @Singleton
    fun providePassPhraseConnection(briteDatabase: BriteDatabase): PassPhraseConnection {
        return PassPhraseConnectionImpl(briteDatabase)
    }

    @Provides
    @Singleton
    fun providePlayingTreasureHuntConnection(briteDatabase: BriteDatabase): PlayingTreasureHuntConnection {
        return PlayingTreasureHuntConnectionImpl(briteDatabase)
    }

    @Provides
    @Singleton
    fun provideTreasureChestConnection(briteDatabase: BriteDatabase): TreasureChestConnection {
        return TreasureChestConnectionImpl(briteDatabase)
    }

    @Provides
    @Singleton
    fun provideTreasureHuntConnection(briteDatabase: BriteDatabase): TreasureHuntConnection {
        return TreasureHuntConnectionImpl(briteDatabase)
    }

    @Provides
    @Singleton
    fun provideWaypointConnection(briteDatabase: BriteDatabase): WaypointConnection {
        return WaypointConnectionImpl(briteDatabase)
    }
}