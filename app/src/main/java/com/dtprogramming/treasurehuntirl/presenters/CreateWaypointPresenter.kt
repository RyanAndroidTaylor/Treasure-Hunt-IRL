package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.CreateWaypointView
import com.dtprogramming.treasurehuntirl.util.randomUuid
import javax.inject.Inject

/**
 * Created by ryantaylor on 6/25/16.
 */
class CreateWaypointPresenter() : Presenter {
    val BASE_NUDGE_DISTANCE = 1.0

    companion object {
        val TAG: String = CreateWaypointPresenter::class.java.simpleName
    }

    @Inject
    lateinit var waypointConnection: WaypointConnection

    private var createWaypointView: CreateWaypointView? = null

    private lateinit var parentUuid: String
    private lateinit var waypointUuid: String

    private var lat: Double = 0.0
    private var lng: Double = 0.0

    private var new = false

    private var zoom = 0f

    private val adjustedNudgeDistance: Double
        get() {
            if (zoom <= 2)
                return BASE_NUDGE_DISTANCE

            var finalNudgeDistance = BASE_NUDGE_DISTANCE
            for (i in 2..zoom.toInt()) {
                finalNudgeDistance *= 0.5
            }

            return finalNudgeDistance
        }

    init {
        THApp.databaseComponent.inject(this)
    }

    fun load(createWaypointView: CreateWaypointView, parentUuid: String) {
        this.createWaypointView = createWaypointView
        this.parentUuid = parentUuid

        loadWaypoint()
    }

    fun reload(createWaypointView: CreateWaypointView) {
        this.createWaypointView = createWaypointView
    }

    override fun unsubscribe() {
        waypointConnection.unsubscribe()

        createWaypointView = null
    }

    override fun dispose() {
        unsubscribe()

        PresenterManager.removePresenter(TAG)
    }

    private fun loadWaypoint() {
        val waypoint = waypointConnection.getWaypointForParent(parentUuid)

        if (waypoint !=  null) {
            waypointUuid = waypoint.uuid
            lat = waypoint.lat
            lng = waypoint.long
        } else {
            new = true
            waypointUuid = randomUuid()
        }
    }

    fun mapLoaded() {
        createWaypointView?.loadMarker(lat, lng)
    }

    fun mapClicked(lat: Double, lng: Double) {
        this.lat = lat
        this.lng = lng

        createWaypointView?.markerMoved(lat, lng)
    }

    fun increaseLat() {
        lat += adjustedNudgeDistance

        createWaypointView?.markerMoved(lat, lng)
    }

    fun decreaseLat() {
        lat -= adjustedNudgeDistance

        createWaypointView?.markerMoved(lat, lng)
    }

    fun increaseLng() {
        lng += adjustedNudgeDistance

        createWaypointView?.markerMoved(lat, lng)
    }

    fun decreaseLng() {
        lng -= adjustedNudgeDistance

        createWaypointView?.markerMoved(lat, lng)
    }

    fun updateZoom(zoom: Float) {
        this.zoom = zoom
    }

    fun cancel() {
        PresenterManager.removePresenter(TAG)

        createWaypointView?.close()
    }

    fun save() {
        if (new)
            waypointConnection.insert(Waypoint(waypointUuid, parentUuid, lat, lng))
        else
            waypointConnection.update(Waypoint(waypointUuid, parentUuid, lat, lng))

        PresenterManager.removePresenter(TAG)

        createWaypointView?.close()
    }
}