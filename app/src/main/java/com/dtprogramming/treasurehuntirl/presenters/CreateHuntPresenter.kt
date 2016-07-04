package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.DatabaseObservables
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import com.dtprogramming.treasurehuntirl.util.randomUuid
import rx.Subscription
import java.util.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateHuntPresenter() : Presenter {

    private var isNewHunt = false
    lateinit var treasureHuntId: String
        private set

    private lateinit var createHuntView: CreateHuntView

    private var clueSubscription: Subscription? = null
    private var waypointSubscription: Subscription? = null

    val clues = ArrayList<Clue>()
    val waypoints = ArrayList<Waypoint>()

    companion object {
        val TAG: String = CreateHuntPresenter::class.java.simpleName
    }

    fun createHunt(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView
        treasureHuntId = UUID.randomUUID().toString().replace("-", "")

        isNewHunt = true

        subscribeToClues()
    }

    fun loadHunt(treasureHuntId: String, createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView
        this.treasureHuntId = treasureHuntId

        isNewHunt = false

        subscribeToClues()
    }

    fun reloadHunt(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView

        subscribeToClues()
    }

    fun mapLoaded() {
        subscribeToWaypoints()
    }

    fun save() {
        if (isNewHunt) {
            val treasureHunt = TreasureHunt(randomUuid(), "Simple Title")

            THApp.briteDatabase.insert(TreasureHunt.TABLE.NAME, treasureHunt.getContentValues())

            for (waypoint in waypoints)
                THApp.briteDatabase.insert(Waypoint.TABLE.NAME, waypoint.getContentValues())

            for (clue in clues)
                THApp.briteDatabase.insert(Clue.TABLE.NAME, clue.getContentValues())
        } else {
            val treasureHunt = TreasureHunt(treasureHuntId, "Simple Title")

            THApp.briteDatabase.update(TreasureHunt.TABLE.NAME, treasureHunt.getContentValues(), TableColumns.WHERE_UUID_EQUALS, treasureHuntId)
        }

        finish()
    }

    fun cancel() {
        finish()
    }

    fun finish() {
        unsubscribeToClues()
        unsubscribeToWaypoints()

        PresenterManager.removePresenter(TAG)

        createHuntView.close()
    }

//    private fun loadContainer() {
//        when (state) {
//            CREATE_HUNT -> {
//                createHuntView.moveToContainer(CreateHuntContainer(this))
//
//                subscribeToClues()
//            }
//            CREATE_CLUE -> {
//                unsubscribeToClues()
//                unsubscribeToWaypoints()
//
//                val createCluePresenter = if (PresenterManager.hasPresenter(CreateCluePresenter.TAG))
//                    PresenterManager.getPresenter(CreateCluePresenter.TAG) as CreateCluePresenter
//                else
//                    PresenterManager.addPresenter(CreateCluePresenter.TAG, CreateCluePresenter()) as CreateCluePresenter
//
//                val createClueContainer = CreateClueContainer(createCluePresenter)
//
//                createCluePresenter.load(createClueContainer, this)
//
//                createHuntView.moveToContainer(createClueContainer)
//            }
//            CREATE_WAY_POINT -> {
//                unsubscribeToClues()
//
//                val createWaypointPresenter = if (PresenterManager.hasPresenter(CreateWaypointPresenter.TAG))
//                    PresenterManager.getPresenter(CreateWaypointPresenter.TAG) as CreateWaypointPresenter
//                else
//                    PresenterManager.addPresenter(CreateWaypointPresenter.TAG, CreateWaypointPresenter()) as CreateWaypointPresenter
//
//                val createWaypointContainer = CreateWayPointContainer(createWaypointPresenter)
//
//                createWaypointPresenter.load(createWaypointContainer, this)
//
//                createHuntView.moveToContainer(createWaypointContainer)
//            }
//        }
//    }

    private fun subscribeToClues() {
        clueSubscription = DatabaseObservables.getClueObservable(treasureHuntId)
                .subscribe ({
                    for (clue in it) {
                        clues.add(clue)
                    }

                    createHuntView.updateClueList(clues)

                    unsubscribeToClues()
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
            for (waypoint in it) {
                waypoints.add(waypoint)
            }

            createHuntView.updateWaypoints(waypoints)

            unsubscribeToWaypoints()
        })
    }

    private fun unsubscribeToWaypoints() {
        waypointSubscription?.let {
            if (!it.isUnsubscribed)
                it.unsubscribe()
        }
    }

    //TODO Look into just adding the one item to the RecyclerView instead of reloading the entire list
    fun saveClue(clue: Clue) {
        clues.add(clue)

        if (!isNewHunt)
            THApp.briteDatabase.insert(Clue.TABLE.NAME, clue.getContentValues())
    }

    fun saveWaypoint(waypoint: Waypoint) {
        waypoints.add(waypoint)

        if (!isNewHunt)
            THApp.briteDatabase.insert(Waypoint.TABLE.NAME, waypoint.getContentValues())
    }
}