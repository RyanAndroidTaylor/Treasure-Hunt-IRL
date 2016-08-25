package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.connections.impl.*
import com.dtprogramming.treasurehuntirl.database.models.InventoryItem
import com.dtprogramming.treasurehuntirl.presenters.PlayTreasureHuntPresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter.InventoryAdapter
import com.dtprogramming.treasurehuntirl.ui.views.PlayTreasureHuntView
import com.dtprogramming.treasurehuntirl.util.NEW
import com.dtprogramming.treasurehuntirl.util.PLAYING_HUNT_UUID
import kotlinx.android.synthetic.main.play_treasure_hunt_container.view.*

/**
 * Created by ryantaylor on 7/19/16.
 */
class PlayTreasureHuntContainer : BaseContainer(), PlayTreasureHuntView {

    companion object {
        val URI: String = PlayTreasureHuntContainer::class.java.simpleName
    }

    override var rootViewId = R.layout.play_treasure_hunt_container

    private var playTreasureHuntPresenter: PlayTreasureHuntPresenter

    private lateinit var inventoryList: RecyclerView
    private lateinit var adapter: InventoryAdapter

    private lateinit var currentTreasureChestAction: Button

    init {
        playTreasureHuntPresenter = if (PresenterManager.hasPresenter(PlayTreasureHuntPresenter.TAG))
            PresenterManager.getPresenter(PlayTreasureHuntPresenter.TAG) as PlayTreasureHuntPresenter
        else
            PresenterManager.addPresenter(PlayTreasureHuntPresenter.TAG, PlayTreasureHuntPresenter()) as PlayTreasureHuntPresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        containerActivity.setToolBarTitle(containerActivity.stringFrom(R.string.play_treasure_hunt_action_bar_title))

        inventoryList = parent.play_treasure_hunt_inventory_list

        inventoryList.layoutManager = LinearLayoutManager(containerActivity)

        adapter = InventoryAdapter(mutableListOf(), { inventoryItemSelected(it) })

        inventoryList.adapter = adapter

        currentTreasureChestAction = parent.play_treasure_hunt_chest_action

        if (extras.containsKey(PLAYING_HUNT_UUID)) {
            if (extras.containsKey(NEW))
                playTreasureHuntPresenter.start(this, extras.getString(PLAYING_HUNT_UUID))
            else
                playTreasureHuntPresenter.load(this, extras.getString(PLAYING_HUNT_UUID))
        } else {
            playTreasureHuntPresenter.reload(this)
        }

        currentTreasureChestAction.setOnClickListener { playTreasureHuntPresenter.performTreasureChestAction() }

        return this
    }

    override fun onPause() {
        super.onPause()

        playTreasureHuntPresenter.unsubscribe()
    }

    override fun onReload(parent: ViewGroup) {
        super.onReload(parent)

        playTreasureHuntPresenter.reload(this)
    }

    override fun onFinish() {
        super.onFinish()

        playTreasureHuntPresenter.dispose()
    }

    override fun hideTreasureChestAction() {
        currentTreasureChestAction.visibility = View.GONE
    }

    override fun displayBuriedTreasureChestAction() {
        currentTreasureChestAction.visibility = View.VISIBLE
        currentTreasureChestAction.text = "Dig"
    }

    override fun displayLockedTreasureChestAction() {
        currentTreasureChestAction.visibility = View.VISIBLE
        currentTreasureChestAction.text = "Unlock"
    }

    override fun updateInventoryList(items: List<InventoryItem>) {
        adapter.updateList(items)
    }

    override fun switchToDigMode() {
        DigModeContainer.start(containerActivity, playTreasureHuntPresenter.playingTreasureHuntUuid)
    }

    override fun viewCollectedTreasureChest(treasureChestUuid: String) {
        ViewCollectedTreasureChestContainer.start(containerActivity, treasureChestUuid)
    }

    private fun inventoryItemSelected(inventoryItem: InventoryItem) {

    }
}