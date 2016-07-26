package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.util.TREASURE_CHEST_UUID
import kotlinx.android.synthetic.main.container_view_treasure_chest.view.*

/**
 * Created by ryantaylor on 7/25/16.
 */
class ViewCollectedTreasureChestContainer : BasicContainer() {

    companion object {
        val URI: String = ViewCollectedTreasureChestContainer::class.java.simpleName

        fun start(containerActivity: ContainerActivity, treasureChestUuid: String) {
            val extras = Bundle()

            extras.putString(TREASURE_CHEST_UUID, treasureChestUuid)

            containerActivity.startContainerAsPopup(ViewCollectedTreasureChestContainer.URI, extras)
        }
    }

    private lateinit var treasureChestImage: ImageView

    override var rootViewId = R.layout.container_view_treasure_chest

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)

        treasureChestImage = parent.view_treasure_chest_container_image

        treasureChestImage.setOnClickListener { openTreasureChest() }

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

    private fun openTreasureChest() {
        treasureChestImage.setImageDrawable(containerActivity.resources.getDrawable(R.drawable.open_chest))
    }
}