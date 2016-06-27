package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.CreateWaypointView
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*

/**
 * Created by ryantaylor on 6/25/16.
 */
class CreateWaypointPresenter() : Presenter {
    val NUDGE_DISTANCE = 0.00001

    private lateinit var createWaypointView: CreateWaypointView

    private lateinit var createHuntPresenter: CreateHuntPresenter

    private lateinit var marker: Marker
        private set

    companion object {
        val TAG = CreateWaypointPresenter::class.java.simpleName
    }

    fun load(createWaypointView: CreateWaypointView, createHuntPresenter: CreateHuntPresenter) {
        this.createWaypointView = createWaypointView
        this.createHuntPresenter = createHuntPresenter
    }

    fun mapLoaded() {
        marker = createWaypointView.loadMarker(MarkerOptions().title("New Marker").position(LatLng(0.0, 0.0)))
    }

    fun mapClicked(latLng: LatLng) {
        marker.position = latLng

        createWaypointView.markerMoved(marker)
    }

    fun titleTextChanged(title: String) {
        marker.title = title
    }

    fun increaseLat() {
        val lat = marker.position.latitude + NUDGE_DISTANCE

        mapClicked(LatLng(lat, marker.position.longitude))
    }

    fun decreaseLat() {
        val lat = marker.position.latitude - NUDGE_DISTANCE

        mapClicked(LatLng(lat, marker.position.longitude))
    }

    fun increaseLng() {
        val lng = marker.position.longitude + NUDGE_DISTANCE

        mapClicked(LatLng(marker.position.latitude, lng))
    }

    fun decreaseLng() {
        val lng = marker.position.longitude - NUDGE_DISTANCE

        mapClicked(LatLng(marker.position.latitude, lng))
    }

    fun cancel() {
        PresenterManager.removePresenter(TAG)

        createHuntPresenter.switchState(CreateHuntPresenter.CREATE_HUNT)
    }

    fun save() {
        val waypoint = Waypoint(UUID.randomUUID().toString().replace("-", ""), marker.title, createHuntPresenter.treasureHuntId, marker.position.latitude, marker.position.longitude)

        THApp.briteDatabase.insert(Waypoint.TABLE.NAME, waypoint.getContentValues())

        PresenterManager.removePresenter(TAG)

        createHuntPresenter.switchState(CreateHuntPresenter.CREATE_HUNT)
    }
}