package com.dtprogramming.treasurehuntirl.ui.container

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.connections.impl.WaypointConnectionImpl
import com.dtprogramming.treasurehuntirl.presenters.CreateWaypointPresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.views.AdjustableValueView
import com.dtprogramming.treasurehuntirl.ui.views.CreateWaypointView
import com.dtprogramming.treasurehuntirl.util.PARENT_UUID
import com.dtprogramming.treasurehuntirl.util.format
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.container_create_waypoint.view.*

/**
 * Created by ryantaylor on 6/22/16.
 */
class CreateWayPointContainer() : BasicContainer(), CreateWaypointView, OnMapReadyCallback {

    companion object {
        val URI: String = CreateWayPointContainer::class.java.simpleName
    }

    override var rootViewId = R.layout.container_create_waypoint

    private lateinit var createWaypointPresenter: CreateWaypointPresenter

    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker

    private lateinit var adjustableLat: AdjustableValueView
    private lateinit var adjustableLng: AdjustableValueView

    init {
        createWaypointPresenter = if (PresenterManager.hasPresenter(CreateWaypointPresenter.TAG))
            PresenterManager.getPresenter(CreateWaypointPresenter.TAG) as CreateWaypointPresenter
        else
            PresenterManager.addPresenter(CreateWaypointPresenter.TAG, CreateWaypointPresenter()) as CreateWaypointPresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        containerActivity.setToolBarTitle(containerActivity.stringFrom(R.string.waypoint_action_bar_title))

        if (extras.containsKey(PARENT_UUID))
            createWaypointPresenter.load(this, extras.getString(PARENT_UUID))
        else
            createWaypointPresenter.reload(this)

        val mapFragment = MapFragment()

        containerActivity.fragmentManager.beginTransaction().replace(R.id.create_waypoint_container_map_container, mapFragment).commit()

        mapFragment.getMapAsync(this)

        adjustableLat = parent.create_waypoint_container_adjust_lat
        adjustableLng = parent.create_waypoint_container_adjust_lng

        adjustableLat.setOnLeftDrawableClickListener { createWaypointPresenter.decreaseLat() }
        adjustableLat.setOnRightDrawableClickListener { createWaypointPresenter.increaseLat() }
        adjustableLng.setOnLeftDrawableClickListener { createWaypointPresenter.decreaseLng() }
        adjustableLng.setOnRightDrawableClickListener { createWaypointPresenter.increaseLng() }

        parent.create_waypoint_container_save.setOnClickListener { createWaypointPresenter.save() }
        parent.create_waypoint_container_cancel.setOnClickListener { createWaypointPresenter.cancel() }

        return this
    }

    override fun onPause() {
        super.onPause()

        createWaypointPresenter.unsubscribe()
    }

    override fun onReload(parent: ViewGroup) {
        super.onReload(parent)

        createWaypointPresenter.reload(this)
    }

    override fun onFinish() {
        super.onFinish()

        createWaypointPresenter.dispose()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap!!

        createWaypointPresenter.mapLoaded()
        createWaypointPresenter.updateZoom(googleMap.cameraPosition.zoom)

        if (ContextCompat.checkSelfPermission(containerActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }

        this.googleMap.setOnMapClickListener { latLng: LatLng ->
            createWaypointPresenter.mapClicked(latLng.latitude, latLng.longitude)
        }

        this.googleMap.setOnCameraChangeListener {
            createWaypointPresenter.updateZoom(it.zoom)
        }
    }

    override fun loadMarker(lat: Double, lng: Double) {
        marker = googleMap.addMarker(MarkerOptions().title("Waypoint").position(LatLng(lat, lng)))

        adjustableLat.mText = "${marker.position.latitude.format(6)}"
        adjustableLng.mText = "${marker.position.longitude.format(6)}"

        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), 12.0f)

        googleMap.moveCamera(cameraUpdate)
    }

    override fun markerMoved(lat: Double, lng: Double) {
        adjustableLat.mText = "${lat.format(6)}"
        adjustableLng.mText = "${lng.format(6)}"

        marker.position = LatLng(lat, lng)
    }

    override fun close() {
        containerActivity.finishCurrentContainer()
    }
}