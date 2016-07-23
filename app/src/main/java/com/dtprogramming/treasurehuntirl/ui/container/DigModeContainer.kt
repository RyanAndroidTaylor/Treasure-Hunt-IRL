package com.dtprogramming.treasurehuntirl.ui.container

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.connections.impl.WaypointConnectionImpl
import com.dtprogramming.treasurehuntirl.presenters.DigModePresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.views.DigModeView
import com.dtprogramming.treasurehuntirl.util.PLAYING_HUNT_UUID
import kotlinx.android.synthetic.main.container_dig_mode.view.*

/**
 * Created by ryantaylor on 7/22/16.
 */
class DigModeContainer : BasicContainer(), DigModeView, LocationListener {

    companion object {
        val URI: String = DigModeContainer::class.java.simpleName
    }

    override var rootViewId = R.layout.container_dig_mode

    private var digModePresenter: DigModePresenter

    private lateinit var locationManager: LocationManager

    init {
        digModePresenter = if (PresenterManager.hasPresenter(DigModePresenter.TAG))
            PresenterManager.getPresenter(DigModePresenter.TAG) as DigModePresenter
        else
            PresenterManager.addPresenter(DigModePresenter.TAG, DigModePresenter(WaypointConnectionImpl())) as DigModePresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)

        parent.dig_mode_container_dig.setOnClickListener { dig() }

        locationManager = containerActivity.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L, 0f, this)

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

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000L, 0f, this)
    }

    override fun onFinish() {
        super.onFinish()

        digModePresenter.dispose()
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        Log.i("DigModeContainer", "Status Changed $status")

        //TODO Warn user of bad connection if status is not good.
    }

    override fun onProviderDisabled(provider: String?) {

    }

    override fun onLocationChanged(location: Location?) {
        location?.let {
            Log.i("DigModeContainer", "Accuracy = ${location.accuracy}")

            digModePresenter.updateLocation(location.latitude, location.longitude)
        }
    }

    override fun onProviderEnabled(provider: String?) {

    }

    override fun displayUnburiedTreasureChest(treasureChestUuid: String) {
        Toast.makeText(containerActivity, treasureChestUuid, Toast.LENGTH_LONG).show()
    }

    fun dig() {
//        playTreasureHuntPresenter.dig(location.latitude, location.longitude)
        //TODO Need to make sure location is accurate before digging and warn user if there is problems getting an accurate location
        digModePresenter.dig()
    }
}