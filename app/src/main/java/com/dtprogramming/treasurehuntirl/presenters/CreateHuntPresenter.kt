package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.DatabaseObservables
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.container.CreateClueContainer
import com.dtprogramming.treasurehuntirl.ui.container.CreateHuntContainer
import com.dtprogramming.treasurehuntirl.ui.container.CreateWayPointContainer
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import rx.Subscription
import java.util.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateHuntPresenter() : Presenter {

    private var state = CREATE_HUNT

    lateinit var treasureHuntId: String
        private set
    private lateinit var createHuntView: CreateHuntView

    private var clueSubscription: Subscription? = null
    private var waypointSubscription: Subscription? = null

    var clues = ArrayList<String>()
        private set
    var waypoints = ArrayList<Waypoint>()

    companion object {
        val TAG = CreateHuntPresenter::class.java.simpleName

        val CREATE_HUNT = 0
        val CREATE_CLUE = 1
        val CREATE_WAY_POINT = 2
    }

    fun load(treasureHuntId: String, createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView
        this.treasureHuntId = treasureHuntId

        createHuntView.initLoad()

        subscribeToClues()
    }

    fun reload(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView

        loadContainer()

        subscribeToClues()
    }

    fun mapLoaded() {
        subscribeToWaypoints()
    }

    fun saveAndFinish() {
        //TODO Save treasure hunt once it is setup

        finish()
    }

    fun finish() {
        unsubscribeToClues()
        unsubscribeToWaypoints()

        PresenterManager.removePresenter(TAG)
    }

    private fun loadContainer() {
        when (state) {
            CREATE_HUNT -> {
                createHuntView.moveToContainer(CreateHuntContainer(this))

                subscribeToClues()
            }
            CREATE_CLUE -> {
                unsubscribeToClues()
                unsubscribeToWaypoints()

                val createCluePresenter = if (PresenterManager.hasPresenter(CreateCluePresenter.TAG))
                    PresenterManager.getPresenter(CreateCluePresenter.TAG) as CreateCluePresenter
                else
                    PresenterManager.addPresenter(CreateCluePresenter.TAG, CreateCluePresenter()) as CreateCluePresenter

                val createClueContainer = CreateClueContainer(createCluePresenter)

                createCluePresenter.load(createClueContainer, this)

                createHuntView.moveToContainer(createClueContainer)
            }
            CREATE_WAY_POINT -> {
                unsubscribeToClues()

                val createWaypointPresenter = if (PresenterManager.hasPresenter(CreateWaypointPresenter.TAG))
                    PresenterManager.getPresenter(CreateWaypointPresenter.TAG) as CreateWaypointPresenter
                else
                    PresenterManager.addPresenter(CreateWaypointPresenter.TAG, CreateWaypointPresenter()) as CreateWaypointPresenter

                val createWaypointContainer = CreateWayPointContainer(createWaypointPresenter)

                createWaypointPresenter.load(createWaypointContainer, this)

                createHuntView.moveToContainer(createWaypointContainer)
            }
        }
    }

    private fun subscribeToClues() {
        clueSubscription = DatabaseObservables.getClueObservable(treasureHuntId)
                .subscribe ({
                    val clueList = ArrayList<String>()

                    for (clue in it) {
                        clueList.add(clue.text)
                    }

                    clues = clueList

                    createHuntView.updateClueList(clues)
                })
    }

    private fun unsubscribeToClues() {
        clueSubscription?.let {
            if (!it.isUnsubscribed)
                it.unsubscribe()
        }
    }

    private fun subscribeToWaypoints() {
        waypointSubscription = DatabaseObservables.getWaypointObservable(treasureHuntId)
        .subscribe({
            val waypointList = ArrayList<Waypoint>()

            for (waypoint in it) {
                waypointList.add(waypoint)
            }

            waypoints = waypointList

            createHuntView.updateWaypoints(waypoints)
        })
    }

    private fun unsubscribeToWaypoints() {
        waypointSubscription?.let {
            if (!it.isUnsubscribed)
                it.unsubscribe()
        }
    }

    fun switchState(newState: Int) {
        state = newState

        loadContainer()
    }
}