package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.CreateWaypointView
import java.util.*

/**
 * Created by ryantaylor on 6/25/16.
 */
class CreateWaypointPresenter(val waypointConnection: WaypointConnection) : Presenter {
    val BASE_NUDGE_DISTANCE = 1.0

    private lateinit var createWaypointView: CreateWaypointView
    private lateinit var treasureHuntId: String

    private var title: String = "New Waypoint"
    private var lat: Double = 0.0
    private var lng: Double = 0.0

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

    companion object {
        val TAG: String = CreateWaypointPresenter::class.java.simpleName
    }

    fun load(createWaypointView: CreateWaypointView, treasureHuntId: String) {
        this.createWaypointView = createWaypointView
        this.treasureHuntId = treasureHuntId
    }

    fun reload(createWaypointView: CreateWaypointView) {
        this.createWaypointView = createWaypointView
    }

    fun mapLoaded(zoom: Float) {
        this.zoom = zoom

        createWaypointView.loadMarker(title, lat, lng)
    }

    fun mapClicked(lat: Double, lng: Double) {
        this.lat = lat
        this.lng = lng

        createWaypointView.markerMoved(lat, lng)
    }

    fun titleTextChanged(title: String) {
        this.title = title
    }

    fun increaseLat() {
        lat += adjustedNudgeDistance

        createWaypointView.markerMoved(lat, lng)
    }

    fun decreaseLat() {
        lat -= adjustedNudgeDistance

        createWaypointView.markerMoved(lat, lng)
    }

    fun increaseLng() {
        lng += adjustedNudgeDistance

        createWaypointView.markerMoved(lat, lng)
    }

    fun decreaseLng() {
        lng -= adjustedNudgeDistance

        createWaypointView.markerMoved(lat, lng)
    }

    fun updateZoom(zoom: Float) {
        this.zoom = zoom
    }

    fun cancel() {
        PresenterManager.removePresenter(TAG)

        createWaypointView.close()
    }

    fun save() {
        waypointConnection.insert(Waypoint(UUID.randomUUID().toString().replace("-", ""), treasureHuntId, lat, lng))

        PresenterManager.removePresenter(TAG)

        createWaypointView.close()
    }
}