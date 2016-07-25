package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity

/**
 * Created by ryantaylor on 7/25/16.
 */
class UnburiedTreasureContainer : BasicContainer() {

    companion object {
        val URI: String = UnburiedTreasureContainer::class.java.simpleName
    }

    override var rootViewId = R.layout.container_unburied_treasure_chest

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