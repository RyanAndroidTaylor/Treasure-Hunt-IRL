package com.dtprogramming.treasurehuntirl.ui.container

import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.presenters.CreateHuntPresenter
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ClueAdapter
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ClueScrollListener
import com.dtprogramming.treasurehuntirl.ui.recycler_view.CustomLinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.container_create_hunt.view.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateHuntContainer(val createHuntPresenter: CreateHuntPresenter) : BasicContainer(), OnMapReadyCallback {

    companion object {
        val URI = CreateHuntContainer::class.java.simpleName
    }

    private lateinit var adapter: ClueAdapter

    private lateinit var clueList: RecyclerView

    private var googleMap: GoogleMap? = null

    override fun inflate(activity: AppCompatActivity, parent: ViewGroup): Container {
        super.inflate(parent, R.layout.container_create_hunt)

        if (googleMap == null) {
            val mapFragment = MapFragment()
            activity.fragmentManager.beginTransaction().replace(R.id.create_hunt_container_map_container, mapFragment).commit()

            mapFragment.getMapAsync(this)
        }

        return this
    }

    override fun loadViews(parent: ViewGroup) {
        clueList = parent.create_hunt_container_clue_list

        clueList.layoutManager = CustomLinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)

        adapter = ClueAdapter(parent.context, emptyList())

        clueList.adapter = adapter

        parent.create_hunt_container_add_clue.setOnClickListener {
            createHuntPresenter.switchState(CreateHuntPresenter.CREATE_CLUE)
        }

        parent.create_hunt_container_add_waypoint.setOnClickListener {
            createHuntPresenter.switchState(CreateHuntPresenter.CREATE_WAY_POINT)
        }

        parent.create_hunt_container_save.setOnClickListener { createHuntPresenter.save() }

        parent.create_hunt_container_cancel.setOnClickListener { createHuntPresenter.cancel() }

        clueList.addOnScrollListener(ClueScrollListener())
    }

    fun updateClueList(clues: List<Clue>) {
        adapter.updateList(clues)
    }

    fun updateWaypointList(waypoints: List<Waypoint>) {
        googleMap?.clear()

        if (waypoints.size > 0) {
            val latLngBoundsBuilder = LatLngBounds.Builder()

            for (waypoint in waypoints) {
                val latLng = LatLng(waypoint.lat, waypoint.long)

                googleMap?.addMarker(MarkerOptions().title(waypoint.title).position(latLng))

                latLngBoundsBuilder.include(latLng)
            }

            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(), 100)

            googleMap?.moveCamera(cameraUpdate)
        }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap

        createHuntPresenter.mapLoaded()
    }
}