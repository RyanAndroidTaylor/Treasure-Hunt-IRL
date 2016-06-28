package com.dtprogramming.treasurehuntirl.ui.container

import android.Manifest
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.presenters.CreateWaypointPresenter
import com.dtprogramming.treasurehuntirl.ui.views.CreateWaypointView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.container_create_waypoint.view.*
import java.lang.ref.WeakReference

/**
 * Created by ryantaylor on 6/22/16.
 */
class CreateWayPointContainer(val createWaypointPresenter: CreateWaypointPresenter) : BasicContainer(), CreateWaypointView, OnMapReadyCallback {

    private lateinit var activity: WeakReference<AppCompatActivity>

    private lateinit var googleMap: GoogleMap
    private lateinit var marker: Marker

    private lateinit var editTitle: EditText
    private lateinit var displayLat: TextView
    private lateinit var displayLng: TextView

    override fun inflate(activity: AppCompatActivity, parent: ViewGroup): Container {
        this.activity = WeakReference<AppCompatActivity>(activity)

        return super.inflate(parent, R.layout.container_create_waypoint)
    }

    override fun loadViews(parent: ViewGroup) {
        val mapFragment = MapFragment()

        activity.get().fragmentManager.beginTransaction().replace(R.id.create_waypoint_container_map_container, mapFragment).commit()

        mapFragment.getMapAsync(this)

        editTitle = parent.create_waypoint_container_title
        displayLat = parent.create_waypoint_container_lat
        displayLng = parent.create_waypoint_container_lng

        parent.create_waypoint_container_inc_lat.setOnClickListener { createWaypointPresenter.increaseLat() }
        parent.create_waypoint_container_dec_lat.setOnClickListener { createWaypointPresenter.decreaseLat() }
        parent.create_waypoint_container_inc_lng.setOnClickListener { createWaypointPresenter.increaseLng() }
        parent.create_waypoint_container_dec_lng.setOnClickListener { createWaypointPresenter.decreaseLng() }
        parent.create_waypoint_container_save.setOnClickListener { createWaypointPresenter.save() }
        parent.create_waypoint_container_cancel.setOnClickListener { createWaypointPresenter.cancel() }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap!!

        createWaypointPresenter.mapLoaded()

        if (ContextCompat.checkSelfPermission(activity.get(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
        }

        this.googleMap.setOnMapClickListener { latLng: LatLng ->
            createWaypointPresenter.mapClicked(latLng.latitude, latLng.longitude)
        }

        editTitle.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                createWaypointPresenter.titleTextChanged(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun loadMarker(title: String, lat: Double, lng: Double) {
        marker = googleMap.addMarker(MarkerOptions().title(title).position(LatLng(lat, lng)))

        editTitle.text.replace(0, editTitle.text.length, marker.title)

        displayLat.text = "${marker.position.latitude}"
        displayLng.text = "${marker.position.longitude}"
    }

    override fun markerMoved(lat: Double, lng: Double) {
        displayLat.text = "$lat"
        displayLng.text = "$lng"

        marker.position = LatLng(lat, lng)
    }

    override fun onBackPressed(): Boolean {
        createWaypointPresenter.cancel()

        return true
    }
}