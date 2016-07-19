package com.dtprogramming.treasurehuntirl.ui.container

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.connections.impl.TreasureChestConnectionImpl
import com.dtprogramming.treasurehuntirl.database.connections.impl.TreasureHuntConnectionImpl
import com.dtprogramming.treasurehuntirl.database.connections.impl.WaypointConnectionImpl
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.presenters.ViewTreasureHuntPresenter
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.views.ViewTreasureHuntView
import com.dtprogramming.treasurehuntirl.util.HUNT_UUID
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.container_view_treasure_hunt.view.*

/**
 * Created by ryantaylor on 7/16/16.
 */
class ViewTreasureHuntContainer : BasicContainer(), ViewTreasureHuntView, OnMapReadyCallback {

    companion object {
        val URI: String = ViewTreasureHuntContainer::class.java.simpleName
    }

    private lateinit var viewTreasureHuntPresenter: ViewTreasureHuntPresenter

    private var googleMap: GoogleMap? = null

    init {
        viewTreasureHuntPresenter = if (PresenterManager.hasPresenter(ViewTreasureHuntPresenter.TAG))
            PresenterManager.getPresenter(ViewTreasureHuntPresenter.TAG) as ViewTreasureHuntPresenter
        else
            PresenterManager.addPresenter(ViewTreasureHuntPresenter.TAG, ViewTreasureHuntPresenter(TreasureHuntConnectionImpl(), TreasureChestConnectionImpl(), WaypointConnectionImpl())) as ViewTreasureHuntPresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        inflateView(R.layout.container_view_treasure_hunt)
        containerActivity.setToolBarTitle(containerActivity.stringFrom(R.string.treasure_hunt_action_bar_title))

        viewTreasureHuntPresenter.load(this, extras.getString(HUNT_UUID))

        val mapFragment = MapFragment()
        containerActivity.fragmentManager.beginTransaction().replace(R.id.view_treasure_hunt_container_map_container, mapFragment).commit()

        mapFragment.getMapAsync(this)

        return this
    }

    override fun onPause() {
        super.onPause()

        viewTreasureHuntPresenter.unsubscribe()
    }

    override fun onReload(parent: ViewGroup) {
        super.onReload(parent)

        viewTreasureHuntPresenter.reload(this)
    }

    override fun onFinish() {
        viewTreasureHuntPresenter.finish()
    }

    override fun displayArea(lat: Double, lng: Double, radius: Double, zoom: Float) {
        googleMap?.let {
            Log.i("ViewTHContainer", "lat: $lat, lng: $lng, radius: $radius, zoom: $zoom")

            val circleOptions = CircleOptions()
            circleOptions.center(LatLng(lat, lng))
            circleOptions.radius(radius)
            circleOptions.fillColor(Color.argb(75, 0, 0, 200))
            circleOptions.strokeColor(containerActivity.resources.getColor(R.color.colorAccent))

            it.addCircle(circleOptions)

            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(lat, lng), zoom)

            it.moveCamera(cameraUpdate)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap

        viewTreasureHuntPresenter.mapLoaded()
    }
}