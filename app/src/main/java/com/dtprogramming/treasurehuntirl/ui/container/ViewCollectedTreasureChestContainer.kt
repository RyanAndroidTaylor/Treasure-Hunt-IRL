package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.connections.impl.CollectedTreasureChestConnectionImpl
import com.dtprogramming.treasurehuntirl.database.connections.impl.InventoryConnectionImpl
import com.dtprogramming.treasurehuntirl.database.models.InventoryItem
import com.dtprogramming.treasurehuntirl.presenters.Presenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.presenters.ViewCollectedTreasureChestPresenter
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter.InventoryAdapter
import com.dtprogramming.treasurehuntirl.ui.views.ViewCollectedTreasureChestView
import com.dtprogramming.treasurehuntirl.util.TREASURE_CHEST_UUID
import kotlinx.android.synthetic.main.container_view_treasure_chest.view.*

/**
 * Created by ryantaylor on 7/25/16.
 */
class ViewCollectedTreasureChestContainer : BasicContainer(), ViewCollectedTreasureChestView {

    companion object {
        val URI: String = ViewCollectedTreasureChestContainer::class.java.simpleName

        fun start(containerActivity: ContainerActivity, treasureChestUuid: String) {
            val extras = Bundle()

            extras.putString(TREASURE_CHEST_UUID, treasureChestUuid)

            containerActivity.startContainerAsPopup(ViewCollectedTreasureChestContainer.URI, extras)
        }
    }

    override var rootViewId = R.layout.container_view_treasure_chest

    private val viewCollectedTreasureChestPresenter: ViewCollectedTreasureChestPresenter

    private var adapter: InventoryAdapter? = null

    private lateinit var treasureChestImage: ImageView

    private lateinit var treasureChestItems: RecyclerView

    init {
        viewCollectedTreasureChestPresenter = if (PresenterManager.hasPresenter(ViewCollectedTreasureChestPresenter.TAG))
            PresenterManager.getPresenter(ViewCollectedTreasureChestPresenter.TAG) as ViewCollectedTreasureChestPresenter
        else
            PresenterManager.addPresenter(ViewCollectedTreasureChestPresenter.TAG, ViewCollectedTreasureChestPresenter(CollectedTreasureChestConnectionImpl(), InventoryConnectionImpl())) as ViewCollectedTreasureChestPresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)

        treasureChestImage = parent.view_treasure_chest_container_image

        treasureChestItems = parent.view_treasure_chest_treasure_items

        treasureChestItems.layoutManager = LinearLayoutManager(containerActivity)
        adapter = InventoryAdapter(containerActivity, listOf(), { inventoryItemSelected(it) })
        treasureChestItems.adapter = adapter

        treasureChestImage.setOnClickListener { openTreasureChest() }

        if (extras.containsKey(TREASURE_CHEST_UUID))
            viewCollectedTreasureChestPresenter.load(this, extras.getString(TREASURE_CHEST_UUID))
        else
            viewCollectedTreasureChestPresenter.reload(this)

        return this
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onReload(parent: ViewGroup) {
        super.onReload(parent)
    }

    override fun onFinish() {
        super.onFinish()
    }

    override fun displayOpenedTreasureChest() {
        treasureChestImage.setImageDrawable(containerActivity.resources.getDrawable(R.drawable.open_chest))
    }

    override fun displayCollectedItems(collectedItems: List<InventoryItem>) {
        adapter?.updateList(collectedItems)
    }

    private fun openTreasureChest() {
        viewCollectedTreasureChestPresenter.openTreasureChest()
    }

    private fun inventoryItemSelected(inventoryItem: InventoryItem) {

    }
}