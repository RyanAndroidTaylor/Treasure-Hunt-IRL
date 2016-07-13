package com.dtprogramming.treasurehuntirl.ui.container

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.impl.ClueConnectionImpl
import com.dtprogramming.treasurehuntirl.database.connections.impl.TreasureChestConnectionImpl
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.presenters.CreateTreasureChestPresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.activities.CreateHuntActivity
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ClueAdapter
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ClueScrollListener
import com.dtprogramming.treasurehuntirl.ui.recycler_view.CustomLinearLayoutManager
import com.dtprogramming.treasurehuntirl.ui.views.CreateTreasureChestView
import com.dtprogramming.treasurehuntirl.util.CLUE_UUID
import com.dtprogramming.treasurehuntirl.util.HUNT_UUID
import com.dtprogramming.treasurehuntirl.util.NEW
import com.dtprogramming.treasurehuntirl.util.TREASURE_CHEST_UUID
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.container_create_treasure_chest.view.*

/**
 * Created by ryantaylor on 7/11/16.
 */
class CreateTreasureChestContainer : BasicContainer(), CreateTreasureChestView, OnMapReadyCallback {

    companion object {
        val URI: String = CreateTreasureChestContainer::class.java.simpleName
    }

    private var createTreasureChestPresenter: CreateTreasureChestPresenter

    private lateinit var editTitle: EditText

    private lateinit var addClue: Button
    private lateinit var clueContainer: CardView
    private lateinit var clueText: TextView

    private var googleMap: GoogleMap? = null

    init {
        createTreasureChestPresenter = if (PresenterManager.hasPresenter(CreateTreasureChestPresenter.TAG))
            PresenterManager.getPresenter(CreateTreasureChestPresenter.TAG) as CreateTreasureChestPresenter
        else
            PresenterManager.addPresenter(CreateTreasureChestPresenter.TAG, CreateTreasureChestPresenter(TreasureChestConnectionImpl(), ClueConnectionImpl())) as CreateTreasureChestPresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        inflateView(R.layout.container_create_treasure_chest)

        checkForLocationPermission()

        editTitle = parent.create_chest_container_title

        if (googleMap == null) {
            val mapFragment = MapFragment()
            containerActivity.fragmentManager.beginTransaction().replace(R.id.create_chest_container_map_container, mapFragment).commit()

            mapFragment.getMapAsync(this)
        }

        parent.create_chest_container_title.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                createTreasureChestPresenter.titleChanged(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        addClue = parent.create_chest_container_add_clue
        clueContainer = parent.create_chest_container_clue_container
        clueText = parent.create_chest_container_clue_text

        addClue.setOnClickListener {
            val bundle = Bundle()

            bundle.putString(TREASURE_CHEST_UUID, createTreasureChestPresenter.treasureChestId)
            bundle.putBoolean(NEW, true)

            containerActivity.loadContainer(CreateClueContainer.URI, bundle)
        }

        clueContainer.setOnClickListener {
            val bundle = Bundle()

            bundle.putString(TREASURE_CHEST_UUID, createTreasureChestPresenter.treasureChestId)

            containerActivity.loadContainer(CreateClueContainer.URI, bundle)
        }

        parent.create_chest_container_add_waypoint.setOnClickListener { moveToContainer(CreateWayPointContainer.URI) }

        loadPresenter(extras)

        return this
    }

    private fun loadPresenter(extras: Bundle) {
        if (extras.containsKey(TREASURE_CHEST_UUID))
            createTreasureChestPresenter.loadTreasureChest(extras.getString(TREASURE_CHEST_UUID), extras.getString(HUNT_UUID), this)
        else if (extras.containsKey(NEW))
            createTreasureChestPresenter.newTreasureChest(extras.getString(HUNT_UUID), this)
        else
            createTreasureChestPresenter.reload(this)
    }

    private fun checkForLocationPermission() {
        if (ContextCompat.checkSelfPermission(containerActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(containerActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    override fun onMapReady(map: GoogleMap?) {
        this.googleMap = map

        createTreasureChestPresenter.mapLoaded()
    }

    override fun displayClue(clue: Clue) {
        addClue.visibility = View.GONE
        clueContainer.visibility = View.VISIBLE

        clueText.text = clue.text
    }

    override fun updateWaypoints(waypoints: List<Waypoint>) {
        googleMap?.clear()

        if (waypoints.size > 0) {
            val latLngBoundsBuilder = LatLngBounds.Builder()

            for (waypoint in waypoints) {
                val latLng = LatLng(waypoint.lat, waypoint.long)

                googleMap?.addMarker(MarkerOptions().title("Waypoint").position(latLng))

                latLngBoundsBuilder.include(latLng)
            }

            val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(), 200)

            googleMap?.moveCamera(cameraUpdate)
        }
    }

    override fun setTitle(title: String) {
        editTitle.setText(title)
    }

    override fun close() {
        containerActivity.finishCurrentContainer()
    }

    fun moveToContainer(uri: String) {
        val extras = Bundle()

        extras.putString(TREASURE_CHEST_UUID, createTreasureChestPresenter.treasureChestId)

        containerActivity.loadContainer(uri, extras)
    }

    override fun onFinish() {
        createTreasureChestPresenter.finish()
    }
}