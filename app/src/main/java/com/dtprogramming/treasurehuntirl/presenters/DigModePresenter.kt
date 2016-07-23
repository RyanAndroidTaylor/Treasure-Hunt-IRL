package com.dtprogramming.treasurehuntirl.presenters

import android.util.Log
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.DigModeView

/**
 * Created by ryantaylor on 7/22/16.
 */
class DigModePresenter(val waypointConnection: WaypointConnection) : Presenter {

    companion object {
        val TAG: String = DigModePresenter::class.java.simpleName
    }

    private var digModeView: DigModeView? = null

    private lateinit var playingTreasureHuntUuid: String

    private var treasureChestWaypoints: List<Waypoint>? = null

    private var lat = 0.0
    private var lng = 0.0

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
    }

    override fun dispose() {
        PresenterManager.removePresenter(TAG)
    }

    fun updateLocation(lat: Double, lng: Double) {
        this.lat = lat
        this.lng = lng
    }

    fun dig() {
        treasureChestWaypoints?.let {
            for ((id, uuid, parentUuid, lat1, long) in it) {
                Log.i("PlayTHPresenter", "waypoint lat: $lat1, dig lat $lat \nwaypoint lng $long, dig lng $lng")

                if ((lat > lat1 - 0.000150 && lat < lat1 + 0.000150) && (lng > long - 0.000150 && lng < long + 0.00150)) {
                    digModeView?.displayUnburiedTreasureChest("Treasure chest was found!!!")

                    break
                }
            }

            digModeView?.displayUnburiedTreasureChest("Nothing here =(")
        }
    }
}