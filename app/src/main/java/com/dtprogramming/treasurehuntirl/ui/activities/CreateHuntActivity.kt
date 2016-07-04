package com.dtprogramming.treasurehuntirl.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.presenters.CreateHuntPresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.container.Container
import com.dtprogramming.treasurehuntirl.ui.container.CreateHuntContainer
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import kotlinx.android.synthetic.main.activity_create_hunt.*
import java.util.*

/**
 * Created by ryantaylor on 6/15/16.
 */
class CreateHuntActivity : BaseActivity(), CreateHuntView {

    private lateinit var createHuntPresenter: CreateHuntPresenter
    private lateinit var container: Container

    companion object {
        val HUNT_UUID = "HuntUuid"

        fun getIntent(context: Context): Intent {
            return Intent(context, CreateHuntActivity::class.java)
        }

        fun getIntent(context: Context, treasureHuntId: String): Intent {
            val intent = Intent(context, CreateHuntActivity::class.java)

            intent.putExtra(HUNT_UUID, treasureHuntId)

            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_hunt)

        toolbar?.title = resources.getString(R.string.create_hunt_activity_title)

        createHuntPresenter = if (PresenterManager.hasPresenter(CreateHuntPresenter.TAG))
            PresenterManager.getPresenter(CreateHuntPresenter.TAG) as CreateHuntPresenter
        else
            PresenterManager.addPresenter(CreateHuntPresenter.TAG, CreateHuntPresenter()) as CreateHuntPresenter

        if (savedInstanceState == null) {
            if (intent.hasExtra(HUNT_UUID)) {
                createHuntPresenter.loadHunt(intent.getStringExtra(HUNT_UUID), this)
            } else {
                createHuntPresenter.createHunt(this)
            }
        } else {
            createHuntPresenter.reloadHunt(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing)
            createHuntPresenter.finish()
    }

    override fun initLoad() {
        container = CreateHuntContainer(createHuntPresenter)

        container.inflate(this, activity_create_hunt_container)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    override fun moveToContainer(container: Container) {
        this.container = container

        this.container.inflate(this, activity_create_hunt_container)
    }

    override fun updateClueList(clues: List<Clue>) {
        if (container is CreateHuntContainer)
            (container as CreateHuntContainer).updateClueList(clues)
    }

    override fun updateWaypoints(waypoints: List<Waypoint>) {
        if (container is CreateHuntContainer)
            (container as CreateHuntContainer).updateWaypointList(waypoints)
    }

    override fun close() {
        finish()
    }

    override fun onBackPressed() {
        if (!container.onBackPressed())
            super.onBackPressed()
    }
}