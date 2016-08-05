package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
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
class PlayTreasureHuntContainer : BasicContainer(), PlayTreasureHuntView {

    companion object {
        val URI: String = PlayTreasureHuntContainer::class.java.simpleName
    }

    override var rootViewId = R.layout.play_treasure_hunt_container

    private var playTreasureHuntPresenter: PlayTreasureHuntPresenter

    private lateinit var inventoryList: RecyclerView
    private lateinit var adapter: InventoryAdapter

    init {
        playTreasureHuntPresenter = if (PresenterManager.hasPresenter(PlayTreasureHuntPresenter.TAG))
            PresenterManager.getPresenter(PlayTreasureHuntPresenter.TAG) as PlayTreasureHuntPresenter
        else
            PresenterManager.addPresenter(PlayTreasureHuntPresenter.TAG, PlayTreasureHuntPresenter(PlayingTreasureHuntConnectionImpl(), InventoryConnectionImpl(), TreasureChestConnectionImpl(), CollectedTreasureChestConnectionImpl())) as PlayTreasureHuntPresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        containerActivity.setToolBarTitle(containerActivity.stringFrom(R.string.play_treasure_hunt_action_bar_title))

        inventoryList = parent.play_treasure_hunt_inventory_list

        inventoryList.layoutManager = LinearLayoutManager(containerActivity)

        adapter = InventoryAdapter(listOf(), { inventoryItemSelected(it) })

        inventoryList.adapter = adapter

        parent.play_treasure_hunt_dig_mode.setOnClickListener { switchToDigMode() }

        if (extras.containsKey(PLAYING_HUNT_UUID)) {
            if (extras.containsKey(NEW))
                playTreasureHuntPresenter.start(this, extras.getString(PLAYING_HUNT_UUID))
            else
                playTreasureHuntPresenter.load(this, extras.getString(PLAYING_HUNT_UUID))
        } else {
            playTreasureHuntPresenter.reload(this)
        }

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

    override fun updateInventoryList(clues: List<InventoryItem>) {
        adapter.updateList(clues)
    }

    private fun switchToDigMode() {

        val extras = Bundle()

        extras.putString(PLAYING_HUNT_UUID, playTreasureHuntPresenter.playingTreasureHuntUuid)

        containerActivity.startContainer(DigModeContainer.URI, extras)
    }

    private fun inventoryItemSelected(inventoryItem: InventoryItem) {

    }
}