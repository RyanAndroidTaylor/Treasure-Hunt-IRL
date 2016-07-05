package com.dtprogramming.treasurehuntirl.ui.container

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.Toast
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.presenters.CreateHuntPresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.activities.CreateHuntActivity
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ClueAdapter
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ClueScrollListener
import com.dtprogramming.treasurehuntirl.ui.recycler_view.CustomLinearLayoutManager
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
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
class CreateHuntContainer() : BasicContainer(), CreateHuntView, OnMapReadyCallback {

    companion object {
        val URI: String = CreateHuntContainer::class.java.simpleName
    }

    val createHuntPresenter: CreateHuntPresenter

    private lateinit var adapter: ClueAdapter

    private lateinit var clueList: RecyclerView

    private var googleMap: GoogleMap? = null

    init {
        createHuntPresenter = if (PresenterManager.hasPresenter(CreateHuntPresenter.TAG))
            PresenterManager.getPresenter(CreateHuntPresenter.TAG) as CreateHuntPresenter
        else
            PresenterManager.addPresenter(CreateHuntPresenter.TAG, CreateHuntPresenter()) as CreateHuntPresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        inflateView(R.layout.container_create_hunt)

        clueList = parent.create_hunt_container_clue_list

        clueList.layoutManager = CustomLinearLayoutManager(parent.context, LinearLayoutManager.HORIZONTAL, false)

        adapter = ClueAdapter(parent.context, emptyList())

        clueList.adapter = adapter

        clueList.addOnScrollListener(ClueScrollListener())

        if (extras.containsKey(CreateHuntActivity.HUNT_UUID)) {
            createHuntPresenter.loadHunt(extras.getString(CreateHuntActivity.HUNT_UUID), this)
        } else if (extras.containsKey(CreateHuntActivity.CREATE_NEW)) {
            createHuntPresenter.createHunt(this)
        } else {
            createHuntPresenter.reloadHunt(this)
        }

        if (ContextCompat.checkSelfPermission(containerActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(containerActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        if (googleMap == null) {
            val mapFragment = MapFragment()
            containerActivity.fragmentManager.beginTransaction().replace(R.id.create_hunt_container_map_container, mapFragment).commit()

            mapFragment.getMapAsync(this)
        }

        //TODO Added edit text for title of Treasure Hunt. Then notify presenter every time the title changes

        parent.create_hunt_container_save.setOnClickListener { createHuntPresenter.save() }

        parent.create_hunt_container_cancel.setOnClickListener { createHuntPresenter.cancel() }

        parent.create_hunt_container_add_clue.setOnClickListener { moveToContainer(CreateClueContainer.URI) }

        parent.create_hunt_container_add_waypoint.setOnClickListener { moveToContainer(CreateWayPointContainer.URI) }

        return this
    }

    fun moveToContainer(uri: String) {
        val extras = Bundle()

        extras.putString(TableColumns.UUID, createHuntPresenter.treasureHuntId)

        containerActivity.loadContainer(uri, extras)
    }

    override fun updateClueList(clues: List<Clue>) {
        adapter.updateList(clues)
    }

    override fun updateWaypoints(waypoints: List<Waypoint>) {
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

    override fun error(message: String) {
        Toast.makeText(containerActivity, message, Toast.LENGTH_LONG).show()
    }

    override fun finish() {
        containerActivity.finish()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap

        createHuntPresenter.mapLoaded()
    }
}