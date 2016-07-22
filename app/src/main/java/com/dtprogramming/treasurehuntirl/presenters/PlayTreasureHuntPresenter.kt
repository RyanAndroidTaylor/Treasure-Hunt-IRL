package com.dtprogramming.treasurehuntirl.presenters

import android.util.Log
import com.dtprogramming.treasurehuntirl.database.connections.*
import com.dtprogramming.treasurehuntirl.database.models.CollectedClue
import com.dtprogramming.treasurehuntirl.database.models.PlayingTreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.PlayTreasureHuntView

/**
 * Created by ryantaylor on 7/19/16.
 */
class PlayTreasureHuntPresenter(val playingTreasureHuntConnection: PlayingTreasureHuntConnection, val connectedClueConnection: CollectedClueConnection, val clueConnection: ClueConnection, val waypointConnection: WaypointConnection) : Presenter {

    private var playTreasureHuntView: PlayTreasureHuntView? = null

    private lateinit var playingTreasureHuntUuid: String

    private var treasureChestWaypoints: List<Waypoint>? = null

    companion object {
        val TAG: String = PlayTreasureHuntPresenter::class.java.simpleName
    }

    fun start(playTreasureHuntView: PlayTreasureHuntView, treasureHuntUuid: String) {
        this.playTreasureHuntView = playTreasureHuntView
        this.playingTreasureHuntUuid = treasureHuntUuid

        playingTreasureHuntConnection.insert(PlayingTreasureHunt(treasureHuntUuid))
        saveInitialClueToCollectedClues()

        loadData()
    }

    fun load(playTreasureHuntView: PlayTreasureHuntView, playingTreasureHuntUuid: String) {
        this.playTreasureHuntView = playTreasureHuntView
        this.playingTreasureHuntUuid = playingTreasureHuntUuid

        loadData()
    }

    fun reload(playTreasureHuntView: PlayTreasureHuntView) {
        this.playTreasureHuntView = playTreasureHuntView

        connectedClueConnection.subscribeToCollectedCluesForParentAsync(playingTreasureHuntUuid, { playTreasureHuntView.updateInventoryList(it) })
    }

    private fun loadData() {
        connectedClueConnection.subscribeToCollectedCluesForParentAsync(playingTreasureHuntUuid, { playTreasureHuntView?.updateInventoryList(it) })
        waypointConnection.getWaypointsForTreasureHuntAsync(playingTreasureHuntUuid, { treasureChestWaypoints = it })
    }

    override fun unsubscribe() {
        playTreasureHuntView = null

        playingTreasureHuntConnection.unsubscribe()
        clueConnection.unsubscribe()
        connectedClueConnection.unsubscribe()
        waypointConnection.unsubscribe()
    }

    override fun finish() {
        PresenterManager.removePresenter(TAG)
    }

    private fun saveInitialClueToCollectedClues() {
        val clue = clueConnection.getClueForParent(playingTreasureHuntUuid)

        clue?.let {
            connectedClueConnection.insert(CollectedClue(clue.uuid, playingTreasureHuntUuid))
        }
    }

    fun dig(lat: Double, lng: Double) {
        treasureChestWaypoints?.let {
            for (waypoint in it) {
                Log.i("PlayTHPresenter", "waypoint lat: ${waypoint.lat}, dig lat $lat \nwaypoint lng ${waypoint.long}, dig lng $lng")
                if ((lat > waypoint.lat - 0.000150 && lat < waypoint.lat + 0.000150)
                        && (lng > waypoint.long - 0.000150 && lng < waypoint.long + 0.00150)) {
                    playTreasureHuntView?.displayFoundTreasureChest("Treasure chest was found!!!")
                } else {
                    playTreasureHuntView?.displayFoundTreasureChest("Nothing here =(")
                }
            }
        }
    }
}