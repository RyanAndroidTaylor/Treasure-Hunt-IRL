package com.dtprogramming.treasurehuntirl.ui.container

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.connections.impl.WaypointConnectionImpl
import com.dtprogramming.treasurehuntirl.presenters.DigModePresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.views.DigModeView
import com.dtprogramming.treasurehuntirl.util.PLAYING_HUNT_UUID
import com.dtprogramming.treasurehuntirl.util.TREASURE_CHEST_UUID
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapFragment
import com.google.android.gms.maps.OnMapReadyCallback
import kotlinx.android.synthetic.main.container_dig_mode.view.*

/**
 * Created by ryantaylor on 7/22/16.
 */
class DigModeContainer : BasicContainer(), DigModeView, LocationListener, OnMapReadyCallback {

    companion object {
        val URI: String = DigModeContainer::class.java.simpleName
    }

    override var rootViewId = R.layout.container_dig_mode

    private var digModePresenter: DigModePresenter

    private lateinit var locationManager: LocationManager

    private var googleMap: GoogleMap? = null

    private lateinit var diggingProgressBar: ProgressBar
    private lateinit var diggingProgressBarContainer: FrameLayout

    init {
        digModePresenter = if (PresenterManager.hasPresenter(DigModePresenter.TAG))
            PresenterManager.getPresenter(DigModePresenter.TAG) as DigModePresenter
        else
            PresenterManager.addPresenter(DigModePresenter.TAG, DigModePresenter(WaypointConnectionImpl())) as DigModePresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)

        diggingProgressBar = parent.dig_mode_container_dig_progress
        diggingProgressBarContainer = parent.dig_mode_container_dig_progress_holder

        parent.dig_mode_container_dig.setOnClickListener { dig() }

        locationManager = containerActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000L, 0f, this)

        val mapFragment = MapFragment()

        containerActivity.fragmentManager.beginTransaction().replace(R.id.dig_mode_container_map_container, mapFragment).commit()

        mapFragment.getMapAsync(this)

        if (extras.containsKey(PLAYING_HUNT_UUID)) {
            digModePresenter.load(this, extras.getString(PLAYING_HUNT_UUID))
        } else {
            digModePresenter.reload(this)
        }

        return this
    }

    override fun onPause() {
        super.onPause()

        digModePresenter.unsubscribe()

        locationManager.removeUpdates(this)


    }

    override fun onReload(parent: ViewGroup) {
        super.onReload(parent)

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000L, 1f, this)
    }

    override fun onFinish() {
        super.onFinish()

        digModePresenter.dispose()
    }

    // LocationListener methods
    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.i("DigModeContainer", "Status Changed $status")

        //TODO Warn user of bad connection if status is not good.
    }

    override fun onProviderDisabled(provider: String?) {

    }

    override fun onLocationChanged(location: Location?) {
        location?.let {
            Log.i("DigModeContainer", "Accuracy = ${location.accuracy}")

            digModePresenter.updateLocation(location.latitude, location.longitude, location.accuracy)
        }
    }

    override fun onProviderEnabled(provider: String?) {

    }

    // DigModeView methods
    override fun displayDiggingAnimation(duration: Int) {
        diggingProgressBarContainer.visibility = View.VISIBLE

        diggingProgressBar.max = duration
        diggingProgressBar.progress = 0
    }

    override fun hideDiggingAnimation() {
        diggingProgressBarContainer.visibility = View.GONE
    }

    override fun updateDiggingProgress(progress: Int) {
        diggingProgressBar.progress = progress
    }

    override fun displayUnburiedTreasureChest(treasureChestUuid: String?) {
        if (treasureChestUuid != null) {
            ViewTreasureChestContainer.start(containerActivity, treasureChestUuid)
        } else {
            Toast.makeText(containerActivity, "Nothing was found", Toast.LENGTH_LONG).show()
        }
    }

    // OnMapReadyCallback methods
    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap

        if (ContextCompat.checkSelfPermission(containerActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            googleMap?.isMyLocationEnabled = true
        }
    }

    private fun dig() {
        digModePresenter.startDigging()
    }
}