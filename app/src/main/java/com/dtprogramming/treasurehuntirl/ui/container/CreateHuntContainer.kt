package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import kotlinx.android.synthetic.main.container_create_hunt.view.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateHuntContainer() : BasicContainer(), CreateHuntView {

    companion object {
        val URI: String = CreateHuntContainer::class.java.simpleName
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        inflateView(R.layout.container_create_hunt)

        parent.create_hunt_container_add_chest.setOnClickListener { containerActivity.loadContainer(CreateTreasureChestContainer.URI) }

        //TODO Display all saved treasure chest for this treasure hunt

        return this
    }

    override fun onFinish() {

    }
}