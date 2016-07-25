package com.dtprogramming.treasurehuntirl.presenters

import android.util.Log
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.DigModeView
import com.dtprogramming.treasurehuntirl.util.DiggingTimer
import rx.Subscription

/**
 * Created by ryantaylor on 7/22/16.
 */
class DigModePresenter(val waypointConnection: WaypointConnection) : Presenter {

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

    private var treasureChestWaypoints: List<Waypoint>? = null

    private var diggingTimer = DiggingTimer()
    private var diggingSubscription: Subscription? = null

    private var lat = 0.0
    private var lng = 0.0
    private var accuracy = 0.0f

    fun load(digModeView: DigModeView, playingTreasureHuntUuid: String) {
        this.digModeView = digModeView
        this.playingTreasureHuntUuid = playingTreasureHuntUuid

        waypointConnection.getWaypointsForTreasureHuntAsync(playingTreasureHuntUuid, { treasureChestWaypoints = it })
    }

    fun reload(digModeView: DigModeView) {
        this.digModeView = digModeView

        waypointConnection.getWaypointsForTreasureHuntAsync(playingTreasureHuntUuid, { treasureChestWaypoints = it })
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
        //TODO I think I want to restrict digging to only the current treasure chest. This way you can't get lucky and dig up some random treasure chest
        treasureChestWaypoints?.let {
            for ((id, uuid, parentUuid, lat1, long) in it) {
                Log.i("PlayTHPresenter", "waypoint lat: $lat1, dig lat $lat \nwaypoint lng $long, dig lng $lng, accuracy $accuracy")

                if ((lat > lat1 - 0.000150 && lat < lat1 + 0.000150) && (lng > long - 0.000150 && lng < long + 0.00150)) {
                    digModeView?.displayUnburiedTreasureChest(parentUuid)

                    return
                }
            }

            digModeView?.displayUnburiedTreasureChest(null)
        }
    }
}