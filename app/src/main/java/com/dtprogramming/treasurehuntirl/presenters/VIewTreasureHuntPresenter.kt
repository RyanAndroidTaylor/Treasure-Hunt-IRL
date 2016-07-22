package com.dtprogramming.treasurehuntirl.presenters

import android.location.Location
import com.dtprogramming.treasurehuntirl.database.connections.TreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.ViewTreasureHuntView
import com.dtprogramming.treasurehuntirl.util.Point
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by ryantaylor on 7/16/16.
 */
class ViewTreasureHuntPresenter(private val treasureHuntConnection: TreasureHuntConnection, private val treasureChestConnection: TreasureChestConnection, private val waypointConnection: WaypointConnection) : Presenter {

    companion object {
        val TAG: String = ViewTreasureHuntPresenter::class.java.simpleName
    }

    private var viewTreasureHuntView: ViewTreasureHuntView? = null

    lateinit var treasureHuntUuid: String
        private set
    private lateinit var treasureHunt: TreasureHunt
    private var treasureChestCount = 0

    private var farthestPointOne: Point? = null
    private var farthestPointTwo: Point? = null
    private var centerPoint: Point? = null
    private var radiusInMeters = 0.0
    private var zoom = 12f
    private lateinit var waypoints: List<Waypoint>

    private var loadDataSubscription: Subscription? = null

    fun load(viewTreasureHuntView: ViewTreasureHuntView, treasureHuntId: String) {
        this.viewTreasureHuntView = viewTreasureHuntView
        this.treasureHuntUuid = treasureHuntId

        loadData()
    }

    fun reload(viewTreasureHuntView: ViewTreasureHuntView) {
        this.viewTreasureHuntView = viewTreasureHuntView
    }

    override fun unsubscribe() {
        loadDataSubscription?.let {
            if (!it.isUnsubscribed)
                it.unsubscribe()
        }

        viewTreasureHuntView = null
    }

    override fun finish() {
        unsubscribe()

        PresenterManager.removePresenter(TAG)
    }

    fun mapLoaded() {
        loadData()
    }

    private fun loadData() {
        loadDataSubscription = getLoadDataObservable(treasureHuntUuid).subscribe {
            centerPoint?.let { viewTreasureHuntView?.displayArea(it.lat, it.lng, radiusInMeters, zoom) }

            viewTreasureHuntView?.displayTitle(treasureHunt.title)
            viewTreasureHuntView?.displayTreasureChestCount(treasureChestCount)
        }
    }

    private fun getLoadDataObservable(treasureHuntUuid: String): Observable<String> {
        return Observable.just(treasureHuntUuid)
                .first {
                    treasureHunt = treasureHuntConnection.getTreasureHunt(treasureHuntUuid)

                    treasureChestCount = treasureChestConnection.getTreasureChestCountForTreasureHunt(treasureHuntUuid)
                    waypoints = waypointConnection.getWaypointsForTreasureHunt(treasureHuntUuid)

                    val points = calculateTwoFarthestPoints(waypoints)
                    farthestPointOne = points.first
                    farthestPointTwo = points.second

                    centerPoint = calculateCenterPoint(farthestPointOne, farthestPointTwo)
                    radiusInMeters = calculateTreasureHuntRadius(farthestPointOne, farthestPointTwo)

                    zoom = getZoomForRadius(radiusInMeters)

                    true
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
    }

    private fun calculateTwoFarthestPoints(waypoints: List<Waypoint>): Pair<Point?, Point?> {
        var pointOne: Point? = null
        var pointTwo: Point? = null

        if (waypoints.size == 1) {
            val waypoint = waypoints[0]

            pointOne = Point(waypoint.lat, waypoint.long)
        } else if (waypoints.size == 2) {
            val waypointOne = waypoints[0]
            val waypointTwo = waypoints[1]

            pointOne = Point(waypointOne.lat, waypointOne.long)
            pointTwo = Point(waypointTwo.lat, waypointTwo.long)
        } else if (waypoints.size > 2) {
            val biggestLatWaypoint = compareAndGetSingle(waypoints, { first, second ->
                if (first.biggerLat(second))
                    first
                else
                    second
            })
            val biggestLngWaypoint = compareAndGetSingle(waypoints, { first, second ->
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

            var biggestDistance = smallestLatWaypoint.distance(biggestLatWaypoint)
            pointOne = Point(smallestLatWaypoint.lat, smallestLatWaypoint.long)
            pointTwo = Point(biggestLatWaypoint.lat, biggestLatWaypoint.long)

            if (smallestLatWaypoint.distance(biggestLngWaypoint) > biggestDistance) {
                biggestDistance = smallestLatWaypoint.distance(biggestLngWaypoint)
                pointOne = Point(smallestLatWaypoint.lat, smallestLatWaypoint.long)
                pointTwo = Point(biggestLngWaypoint.lat, biggestLngWaypoint.long)
            }
            if (smallestLngWaypoint.distance(biggestLatWaypoint) > biggestDistance) {
                biggestDistance = smallestLngWaypoint.distance(biggestLatWaypoint)
                pointOne = Point(smallestLngWaypoint.lat, smallestLngWaypoint.long)
                pointTwo = Point(biggestLatWaypoint.lat, biggestLatWaypoint.long)
            }
            if (smallestLngWaypoint.distance(biggestLngWaypoint) > biggestDistance) {
                pointOne = Point(smallestLngWaypoint.lat, smallestLngWaypoint.long)
                pointTwo = Point(biggestLngWaypoint.lat, biggestLngWaypoint.long)
            }
        }

        return Pair(pointOne, pointTwo)
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

                centerPoint = Point(Math.toDegrees(centerLat), Math.toDegrees(centerLng))
            } else {
                centerPoint = Point(pointOne.lat, pointOne.lng)
            }
        }

        return centerPoint
    }

    private fun calculateTreasureHuntRadius(pointOne: Point?, pointTwo: Point?): Double {
        var radiusInMeters = 0.0

        if (pointOne != null && pointTwo != null) {
            val distance = FloatArray(1)

            Location.distanceBetween(pointOne.lat, pointOne.lng, pointTwo.lat, pointTwo.lng, distance)

            radiusInMeters = distance[0].toDouble() / 2

        } else if (pointOne != null) {
            radiusInMeters = 1000.0
        }

        return radiusInMeters
    }

    private fun getZoomForRadius(radius: Double): Float {
        when (radius) {
            in 0..1000 -> return 14f
            in 1001..2000 -> return 13f
            in 2001..4000 -> return 12f
            in 4001..8000 -> return 11f
            in 8001..16000 -> return 10f
            in 16001..32000 -> return 9f
            in 32001..64000 -> return 8f
            in 64001..128000 -> return 7f
            in 128001..256000 -> return 6f
            in 256001..512000 -> return 5f
            in 512001..1240000 -> return 4f
            in 1240001..2480000 -> return 3f
            else -> return 2f
        }
    }

    private fun compareAndGetSingle(waypoints: List<Waypoint>, compare: (first: Waypoint, second: Waypoint) -> Waypoint): Waypoint {
        var biggestLatWaypoint = waypoints[0]

        for (i in 0..waypoints.size - 1) {
            biggestLatWaypoint = compare(biggestLatWaypoint, waypoints[i])
        }

        return biggestLatWaypoint
    }
}