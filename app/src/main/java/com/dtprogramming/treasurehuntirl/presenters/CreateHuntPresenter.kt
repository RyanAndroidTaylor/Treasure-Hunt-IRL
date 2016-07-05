package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.DatabaseObservables
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.getString
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import com.dtprogramming.treasurehuntirl.util.randomUuid
import rx.Subscription
import java.util.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateHuntPresenter() : Presenter {

    private var isNewHunt = false
    private lateinit var treasureHunt: TreasureHunt
    private var treasureHuntTitle = "New Treasure Hunt"
    val treasureHuntId: String
        get() {
            return treasureHunt.uuid
        }

    private lateinit var createHuntView: CreateHuntView

    private var clueSubscription: Subscription? = null
    private var waypointSubscription: Subscription? = null

    val clues = ArrayList<Clue>()
    val waypoints = ArrayList<Waypoint>()

    companion object {
        val TAG: String = CreateHuntPresenter::class.java.simpleName
    }

    fun createHunt(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView
        treasureHunt = TreasureHunt(UUID.randomUUID().toString().replace("-", ""), treasureHuntTitle)

        isNewHunt = true

        THApp.briteDatabase.insert(TreasureHunt.TABLE.NAME, treasureHunt.getContentValues())

        subscribeToClues()
    }

    fun loadHunt(treasureHuntId: String, createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView

        val cursor = THApp.briteDatabase.query("SELECT * FROM ${TreasureHunt.TABLE.NAME} WHERE ${TableColumns.WHERE_UUID_EQUALS}", treasureHuntId)

        if (cursor.moveToFirst()) {
            treasureHunt = TreasureHunt(cursor.getString(TableColumns.UUID), cursor.getString(TreasureHunt.TABLE.TITLE))

            subscribeToClues()
        } else {
            cursor.close()

            createHuntView.error("Unable to find Treasure Hunt")
            createHuntView.finish()
        }

        cursor.close()

        isNewHunt = false
    }

    fun reloadHunt(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView

        subscribeToClues()
    }

    fun mapLoaded() {
        subscribeToWaypoints()
    }

    fun titleChange(title: String) {
        treasureHuntTitle = title
    }

    fun save() {
        THApp.briteDatabase.update(TreasureHunt.TABLE.NAME, treasureHunt.getContentValues(), TableColumns.WHERE_UUID_EQUALS, treasureHuntId)

        finish()
    }

    fun cancel() {
        finish()
    }

    fun finish() {
        unsubscribeToClues()
        unsubscribeToWaypoints()

        PresenterManager.removePresenter(TAG)

        createHuntView.finish()
    }

    private fun subscribeToClues() {
        clueSubscription = DatabaseObservables.getClueObservable(treasureHuntId)
                .subscribe({
                    for (clue in it) {
                        clues.add(clue)
                    }

                    createHuntView.updateClueList(clues)

                    unsubscribeToClues()
                })
    }

    private fun unsubscribeToClues() {
        clueSubscription?.let {
            if (!it.isUnsubscribed)
                it.unsubscribe()
        }
    }

    private fun subscribeToWaypoints() {
        waypointSubscription = DatabaseObservables.getWaypointObservable(treasureHuntId)
                .subscribe({
                    for (waypoint in it) {
                        waypoints.add(waypoint)
                    }

                    createHuntView.updateWaypoints(waypoints)

                    unsubscribeToWaypoints()
                })
    }

    private fun unsubscribeToWaypoints() {
        waypointSubscription?.let {
            if (!it.isUnsubscribed)
                it.unsubscribe()
        }
    }
}