package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.util.TREASURE_CHEST_UUID

/**
 * Created by ryantaylor on 7/25/16.
 */
class ViewTreasureChestContainer : BasicContainer() {

    companion object {
        val URI: String = ViewTreasureChestContainer::class.java.simpleName

        fun start(containerActivity: ContainerActivity, treasureChestUuid: String) {
            val extras = Bundle()

            extras.putString(TREASURE_CHEST_UUID, treasureChestUuid)

            containerActivity.startContainerAsPopup(ViewTreasureChestContainer.URI, extras)
        }
    }

    override var rootViewId = R.layout.container_view_treasure_chest

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)

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
}