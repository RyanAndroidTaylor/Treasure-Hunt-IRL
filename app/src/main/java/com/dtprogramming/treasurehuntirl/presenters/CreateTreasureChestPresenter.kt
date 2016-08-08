package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.connections.PassPhraseConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.PassPhrase
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.ui.views.CreateTreasureChestView
import com.dtprogramming.treasurehuntirl.util.BURIED
import com.dtprogramming.treasurehuntirl.util.BURIED_LOCKED
import com.dtprogramming.treasurehuntirl.util.LOCKED
import com.dtprogramming.treasurehuntirl.util.randomUuid
import javax.inject.Inject

/**
 * Created by ryantaylor on 7/11/16.
 */
class CreateTreasureChestPresenter() : Presenter {

    companion object {
        val TAG: String = CreateTreasureChestPresenter::class.java.simpleName
    }

    @Inject
    lateinit var treasureChestConnection: TreasureChestConnection
    @Inject
    lateinit var clueConnection: ClueConnection
    @Inject
    lateinit var waypointConnection: WaypointConnection
    @Inject
    lateinit var passPhraseConnection: PassPhraseConnection

    private var createTreasureChestView: CreateTreasureChestView? = null

    private var treasureChestTitle = "New Treasure Chest"
        private set

    lateinit var treasureHuntUuid: String
        private set
    lateinit var treasureChestUuid: String
        private set

    private var passPhraseUuid: String? = null
    private var passPhraseText: String? = null

    private var treasureChestOrder = 0
    private var treasureChestState = BURIED

    init {
        THApp.databaseComponent.inject(this)
    }

    fun create(treasureHuntUuid: String, createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView
        this.treasureHuntUuid = treasureHuntUuid

        treasureChestUuid = randomUuid()
        treasureChestOrder = treasureChestConnection.getNextTreasureChestPosition(this.treasureHuntUuid)

        treasureChestConnection.insert(TreasureChest(treasureChestUuid, treasureHuntUuid, treasureChestTitle, treasureChestOrder, BURIED))

        createTreasureChestView.setTitle(treasureChestTitle)
        createTreasureChestView.setState(treasureChestState)

        stateChanged(treasureChestState)
    }

    fun load(treasureChestId: String, createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView
        this.treasureChestUuid = treasureChestId


        loadTreasureChest()
        loadClues()
        loadWaypoint()
    }

    fun reload(createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView

        createTreasureChestView.setTitle(treasureChestTitle)

        loadClues()
        loadWaypoint()
    }

    override fun unsubscribe() {
        treasureChestConnection.unsubscribe()
        clueConnection.unsubscribe()
        waypointConnection.unsubscribe()

        createTreasureChestView = null
    }

    override fun dispose() {
        unsubscribe()

        treasureChestConnection.update(TreasureChest(treasureChestUuid, treasureHuntUuid, treasureChestTitle, treasureChestOrder, treasureChestState))

        if (treasureChestState == LOCKED || treasureChestState == BURIED_LOCKED) {
            passPhraseConnection.insert(PassPhrase(passPhraseUuid!!, treasureChestUuid, passPhraseText!!))
        } else {
            passPhraseConnection.deleteForParent(treasureChestUuid)
        }

        PresenterManager.removePresenter(TAG)
    }

    private fun loadTreasureChest() {
        val treasureChest = treasureChestConnection.getTreasureChest(treasureChestUuid)

        treasureHuntUuid = treasureChest.treasureHuntUuid
        treasureChestTitle = treasureChest.title
        treasureChestOrder = treasureChest.order
        treasureChestState = treasureChest.state

        createTreasureChestView?.setTitle(treasureChestTitle)
        createTreasureChestView?.setState(treasureChestState)

        stateChanged(treasureChestState)
    }

    private fun loadClues() {
        clueConnection.getCluesForParentAsync(treasureChestUuid, { createTreasureChestView?.updateClueList(it) })
    }

    private fun loadWaypoint() {
        val waypoint = waypointConnection.getWaypointForParent(treasureChestUuid)

        createTreasureChestView?.displayWaypointInfo(waypoint)
    }

    private fun loadPassPhrase() {
        val passPhrase = passPhraseConnection.getPassPhraseForParent(treasureChestUuid)

        if (passPhrase == null) {
            if (passPhraseUuid == null)
                this.passPhraseUuid = randomUuid()

            passPhraseText = ""
        } else {
            passPhraseText = passPhrase.text
            passPhraseUuid = passPhrase.uuid
        }

        createTreasureChestView?.displayPassPhraseInfo(passPhrase?.text)
    }

    fun titleChanged(newTitle: String) {
        treasureChestTitle = newTitle
    }

    fun passPhraseChanged(newPassPhraseText: String) {
        passPhraseText = newPassPhraseText
    }

    fun stateChanged(newState: Int) {
        treasureChestState = newState

        when (newState) {
            BURIED -> {
                loadWaypoint()

                createTreasureChestView?.hidePassPhraseInfo()
            }
            LOCKED -> {
                loadPassPhrase()

                createTreasureChestView?.hideWaypointInfo()
            }
            BURIED_LOCKED -> {
                loadWaypoint()
                loadPassPhrase()
            }
        }
    }
}