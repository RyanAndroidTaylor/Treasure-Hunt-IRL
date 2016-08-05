package com.dtprogramming.treasurehuntirl.presenters

import android.util.Log
import com.dtprogramming.treasurehuntirl.database.connections.CollectedTreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.DigModeView
import com.dtprogramming.treasurehuntirl.util.CLOSED
import com.dtprogramming.treasurehuntirl.util.DiggingTimer
import rx.Subscription

/**
 * Created by ryantaylor on 7/22/16.
 */
class DigModePresenter(val waypointConnection: WaypointConnection, val treasureChestConnection: TreasureChestConnection, val collectedTreasureChestConnection: CollectedTreasureChestConnection) : Presenter {

    companion object {
        val TAG: String = DigModePresenter::class.java.simpleName
    }

    //TODO Fine tune accuracy to figure out if we need to warn the user of inaccurate location data
    val PERFECT = 0.0f
    val GOOD = 10.0f
    val ACCEPTABLE = 20.0f
    val BAD = 30.0f

    private var digModeView: DigModeView? = null

    private lateinit var playingTreasureHuntUuid: String

    private lateinit var currentTreasureChest: TreasureChest
    private lateinit var currentTreasureChestLocation: Waypoint

    private var diggingTimer = DiggingTimer()
    private var diggingSubscription: Subscription? = null

    private var lat = 0.0
    private var lng = 0.0
    private var accuracy = 0.0f

    fun load(digModeView: DigModeView, playingTreasureHuntUuid: String) {
        this.digModeView = digModeView
        this.playingTreasureHuntUuid = playingTreasureHuntUuid

        val treasureChest = treasureChestConnection.getCurrentTreasureChest(playingTreasureHuntUuid)

        if (treasureChest != null) {
            currentTreasureChest = treasureChest
            currentTreasureChestLocation = waypointConnection.getWaypointForParent(currentTreasureChest.uuid)!!
        } else {
            //TODO This should never happen but if it does we need to warn the user that there are no more buried treasure chests then close the container
            Log.e("DigModePresenter", "Tried to switch to dig mode when there was no more buried treasure chests")
        }
    }

    fun reload(digModeView: DigModeView) {
        this.digModeView = digModeView
    }

    override fun unsubscribe() {
        digModeView = null

        waypointConnection.unsubscribe()

        diggingSubscription?.let {
            it.unsubscribe()
        }
    }

    override fun dispose() {
        unsubscribe()

        PresenterManager.removePresenter(TAG)
    }

    fun updateLocation(lat: Double, lng: Double, accuracy: Float) {
        Log.i("PlayTHPresenter", "updated lat: $lat, updated lng $lng, accuracy $accuracy")

        this.lat = lat
        this.lng = lng
        this.accuracy = accuracy
    }

    fun startDigging() {
        when (accuracy) {
            in PERFECT..GOOD -> {
                digFor(3000)
            }
            in GOOD..ACCEPTABLE -> {
                digFor(3000)
            }
            in ACCEPTABLE..BAD -> {
                //TODO wait to see if we can get a better location update if one is not received in a reasonable amount of time attempt dig but warn user that location is bad
                digFor(3000)
            }
            else -> {
                //TODO Tell use location is not accurate enough to dig
                digFor(3000)
            }
        }
    }

    private fun digFor(duration: Long) {
        digModeView?.displayDiggingAnimation(duration.toInt())

        diggingSubscription = diggingTimer.StartDiggingTimer(duration, {
            digModeView?.updateDiggingProgress(it.toInt())
        }, {
            diggingSubscription?.unsubscribe()
            digModeView?.hideDiggingAnimation()
            displayFindings()
        })
    }

    private fun displayFindings() {
        val (id, uuid, parentUuid, lat, long) = currentTreasureChestLocation

        if ((lat > lat - 0.000150 && lat < lat + 0.000150) && (lng > long - 0.000150 && lng < long + 0.00150)) {
            collectedTreasureChestConnection.insert(currentTreasureChest.collectTreasureChest())

            digModeView?.displayCollectedTreasureChest(currentTreasureChest.uuid)
        } else {
            digModeView?.displayCollectedTreasureChest(null)
        }
    }
}