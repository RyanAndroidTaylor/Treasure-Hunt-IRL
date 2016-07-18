package com.dtprogramming.treasurehuntirl.presenters

import android.util.Log
import com.dtprogramming.treasurehuntirl.database.connections.TreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.ViewTreasureHuntView
import com.dtprogramming.treasurehuntirl.util.Point

/**
 * Created by ryantaylor on 7/16/16.
 */
class ViewTreasureHuntPresenter(val treasureHuntConnection: TreasureHuntConnection, val treasureChestConnection: TreasureChestConnection, val waypointConnection: WaypointConnection) : Presenter {

    companion object {
        val TAG: String = ViewTreasureHuntPresenter::class.java.simpleName
    }

    private lateinit var viewTreasureHuntView: ViewTreasureHuntView

    private lateinit var treasureHuntId: String
    private lateinit var treasureHunt: TreasureHunt
    private lateinit var treasureChests: List<TreasureChest>

    private var farthestPointOne: Point? = null
    private var farthestPointTwo: Point? = null
    private var centerPoint: Point? = null
    private var radiusInMeters = 0.0

    fun load(viewTreasureHuntView: ViewTreasureHuntView, treasureHuntId: String) {
        this.viewTreasureHuntView = viewTreasureHuntView
        this.treasureHuntId = treasureHuntId

        loadData()
    }

    fun reload(viewTreasureHuntView: ViewTreasureHuntView) {
        this.viewTreasureHuntView = viewTreasureHuntView
    }

    override fun unsubscribe() {

    }

    override fun finish() {

        PresenterManager.removePresenter(TAG)
    }

    private fun loadData() {
        treasureHunt = treasureHuntConnection.getTreasureHunt(treasureHuntId)

        treasureChestConnection.getTreasureChestsForTreasureHuntAsync(treasureHuntId, {
            treasureChests = it

            calculateTwoFarthestPoints({ points: Pair<Point?, Point?> ->
                farthestPointOne = points.first
                farthestPointTwo = points.second

                centerPoint = calculateCenterPoint(farthestPointOne, farthestPointTwo)
                radiusInMeters = calculateTreasureHuntRadius(farthestPointOne, farthestPointTwo)

                centerPoint?.let { viewTreasureHuntView.displayArea(it.lat, it.lng, radiusInMeters) }
            })
        })
    }

    private fun calculateTwoFarthestPoints(onComplete: (Pair<Point?, Point?>) -> Unit) {
        var pointOne: Point? = null
        var pointTwo: Point? = null

        if (treasureChests.size > 0) {
            waypointConnection.getWaypointsForTreasureChestsAsync(treasureChests, {
                val waypoints = it

                if (waypoints.size == 1) {
                    val waypoint = waypoints[0]

                    pointOne = Point(waypoint.lat, waypoint.long)
                } else if (treasureChests.size == 2) {
                    val waypointOne = waypoints[0]
                    val waypointTwo = waypoints[1]

                    pointOne = Point(waypointOne.lat, waypointOne.long)
                    pointTwo = Point(waypointTwo.lat, waypointTwo.long)
                } else if (treasureChests.size > 2) {
                    val biggestLatWaypoint = compareAndGetSingle(waypoints, { first, second ->
                        if (first.biggerLat(second))
                            first
                        else
                            second
                    })
                    val biggestLngWaypiont = compareAndGetSingle(waypoints, { first, second ->
                        if (first.biggerLng(second))
                            first
                        else
                            second
                    })
                    val smallestLatWaypoint = compareAndGetSingle(waypoints, { first, second ->
                        if (first.biggerLat(second))
                            second
                        else
                            first
                    })
                    val smallestLngWaypoint = compareAndGetSingle(waypoints, { first, second ->
                        if (first.biggerLng(second))
                            second
                        else
                            first
                    })
                }
            })
        }

        onComplete(Pair(pointOne, pointTwo))
    }

    private fun calculateCenterPoint(pointOne: Point?, pointTwo: Point?): Point? {
        var centerPoint: Point? = null

        if (pointOne != null) {
            if (pointTwo != null) {
                val dLng = Math.toRadians(pointTwo.lng - pointOne.lng)

                val latOne = Math.toRadians(pointOne.lat)
                val latTwo = Math.toRadians(pointTwo.lat)
                val lngOne = Math.toRadians(pointOne.lng)

                val Bx = Math.cos(latTwo) * Math.cos(dLng)
                val By = Math.cos(latTwo) * Math.sin(dLng)
                val centerLat = Math.atan2(Math.sin(latOne) + Math.sin(latTwo), Math.sqrt((Math.cos(latOne) + Bx) * (Math.cos(latOne) + Bx) + By * By))
                val centerLng = lngOne + Math.atan2(By, Math.cos(latOne) + Bx)

                centerPoint = Point(centerLat, centerLng)
            } else {
                centerPoint = Point(pointOne.lat, pointOne.lng)
            }
        }

        return centerPoint
    }

    private fun calculateTreasureHuntRadius(pointOne: Point?, pointTwo: Point?): Double {
        var radiusInMeters = 0.0

        if (pointOne != null && pointTwo != null) {
            val R = 6378.137
            val dLat = (pointTwo.lat - pointOne.lat) * Math.PI / 180
            val dLng = (pointTwo.lng - pointOne.lng) * Math.PI / 180
            val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(pointOne.lat * Math.PI / 180) * Math.cos(pointTwo.lat * Math.PI / 180) * Math.sin(dLng / 2) * Math.sin(dLng / 2)
            val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
            val d = R * c

            radiusInMeters = d * 1000

        } else if (pointOne != null) {
            radiusInMeters = 1000.0
        }

        Log.i("ViewTHPresenter", "Radius = $radiusInMeters")
        return radiusInMeters
    }

    private fun compareAndGetSingle(waypoints: List<Waypoint>, compare: (first: Waypoint, second: Waypoint) -> Waypoint): Waypoint {
        var biggestLatWaypoint = waypoints[0]

        for (i in 0..waypoints.size - 1) {
            biggestLatWaypoint = compare(biggestLatWaypoint, waypoints[i])
        }

        return biggestLatWaypoint
    }

    fun mapLoaded() {
        loadData()
    }
}