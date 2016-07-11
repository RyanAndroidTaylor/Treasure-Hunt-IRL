package com.dtprogramming.treasurehuntirl.ui.container

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.impl.WaypointConnectionImpl
import com.dtprogramming.treasurehuntirl.presenters.CreateWaypointPresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.views.AdjustableValueView
import com.dtprogramming.treasurehuntirl.ui.views.CreateWaypointView
import com.dtprogramming.treasurehuntirl.util.format
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

    private lateinit var createWaypointPresenter: CreateWaypointPresenter

    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker

    private lateinit var editTitle: EditText

    private lateinit var adjustableLat: AdjustableValueView
    private lateinit var adjustableLng: AdjustableValueView

    init {
        createWaypointPresenter = if (PresenterManager.hasPresenter(CreateWaypointPresenter.TAG))
            PresenterManager.getPresenter(CreateWaypointPresenter.TAG) as CreateWaypointPresenter
        else
            PresenterManager.addPresenter(CreateWaypointPresenter.TAG, CreateWaypointPresenter(WaypointConnectionImpl())) as CreateWaypointPresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        inflateView(R.layout.container_create_waypoint)

        editTitle = parent.create_waypoint_container_title

        createWaypointPresenter.load(this, extras.getString(TableColumns.UUID))

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

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap!!

        createWaypointPresenter.mapLoaded(this.googleMap.cameraPosition.zoom)

        if (ContextCompat.checkSelfPermission(containerActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }

        this.googleMap.setOnMapClickListener { latLng: LatLng ->
            createWaypointPresenter.mapClicked(latLng.latitude, latLng.longitude)
        }

        this.googleMap.setOnCameraChangeListener {
            createWaypointPresenter.updateZoom(it.zoom)
        }

        editTitle.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                createWaypointPresenter.titleTextChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun loadMarker(title: String, lat: Double, lng: Double) {
        marker = googleMap.addMarker(MarkerOptions().title(title).position(LatLng(lat, lng)))

        editTitle.text.replace(0, editTitle.text.length, marker.title)

        adjustableLat.mText = "${marker.position.latitude.format(6)}"
        adjustableLng.mText = "${marker.position.longitude.format(6)}"
    }

    override fun markerMoved(lat: Double, lng: Double) {
        adjustableLat.mText = "${lat.format(6)}"
        adjustableLng.mText = "${lng.format(6)}"

        marker.position = LatLng(lat, lng)
    }

    override fun close() {
        containerActivity.finishCurrentContainer()
    }

    override fun onFinish() {

    }
}