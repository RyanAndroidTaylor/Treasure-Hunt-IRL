package com.dtprogramming.treasurehuntirl.ui.container

import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.presenters.CreateHuntPresenter
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.lang.ref.WeakReference

/**
 * Created by ryantaylor on 6/22/16.
 */
class CreateWayPointContainer(val activity: WeakReference<AppCompatActivity>, val createHuntPresenter: CreateHuntPresenter) : BasicContainer(), OnMapReadyCallback {
    override lateinit var layout: View

    private lateinit var googleMap: GoogleMap

    override fun inflate(parent: ViewGroup): Container {
        return super.inflate(parent, R.layout.container_create_waypoint)
    }

    override fun loadViews(parent: ViewGroup) {
        val mapFragment = MapFragment()
        activity.get().fragmentManager.beginTransaction().replace(R.id.create_waypoint_container_map_container, mapFragment).commit()
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap!!

        this.googleMap.setOnMapClickListener { latLng: LatLng ->
            Log.i("CreateWPContainer", "Map was clicked")
        }

        val sydney = LatLng(-34.0, 151.0);
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }

    override fun onBackPressed(): Boolean {
        createHuntPresenter.switchState(CreateHuntPresenter.CREATE_HUNT)

        return true
    }
}