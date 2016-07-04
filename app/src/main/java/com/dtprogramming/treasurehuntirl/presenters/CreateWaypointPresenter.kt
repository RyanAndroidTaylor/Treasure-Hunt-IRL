package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.ui.views.CreateWaypointView

/**
 * Created by ryantaylor on 6/25/16.
 */
class CreateWaypointPresenter() : Presenter {
    val NUDGE_DISTANCE = 0.00001

    private lateinit var createWaypointView: CreateWaypointView

    private var title: String = "New Waypoint"
    private var lat: Double = 0.0
    private var lng: Double = 0.0

    companion object {
        val TAG = CreateWaypointPresenter::class.java.simpleName
    }

    fun load(createWaypointView: CreateWaypointView) {
        this.createWaypointView = createWaypointView
    }

    fun mapLoaded() {
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
        lat += NUDGE_DISTANCE

        createWaypointView.markerMoved(lat, lng)
    }

    fun decreaseLat() {
        lat -= NUDGE_DISTANCE

        createWaypointView.markerMoved(lat, lng)
    }

    fun increaseLng() {
        lng += NUDGE_DISTANCE

        createWaypointView.markerMoved(lat, lng)
    }

    fun decreaseLng() {
        lng -= NUDGE_DISTANCE

        createWaypointView.markerMoved(lat, lng)
    }

    fun cancel() {
        PresenterManager.removePresenter(TAG)

        createWaypointView.back()
//        createHuntPresenter.switchState(CreateHuntPresenter.CREATE_HUNT)
    }

    fun save() {
//        val waypoint = Waypoint(UUID.randomUUID().toString().replace("-", ""), title, createHuntPresenter.treasureHuntId, lat, lng)

//        createHuntPresenter.saveWaypoint(waypoint)

        PresenterManager.removePresenter(TAG)

        createWaypointView.back()

//        createHuntPresenter.switchState(CreateHuntPresenter.CREATE_HUNT)
    }
}