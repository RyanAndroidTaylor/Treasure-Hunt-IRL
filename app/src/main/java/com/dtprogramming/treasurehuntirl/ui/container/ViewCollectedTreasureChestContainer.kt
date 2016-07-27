package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.view.ViewGroup
import android.widget.ImageView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.connections.impl.CollectedTreasureChestConnectionImpl
import com.dtprogramming.treasurehuntirl.presenters.Presenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.presenters.ViewCollectedTreasureChestPresenter
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
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

    private val viewCollectedTreasureChestPresenter: ViewCollectedTreasureChestPresenter

    private lateinit var treasureChestImage: ImageView

    override var rootViewId = R.layout.container_view_treasure_chest

    init {
        viewCollectedTreasureChestPresenter = if (PresenterManager.hasPresenter(ViewCollectedTreasureChestPresenter.TAG))
            PresenterManager.getPresenter(ViewCollectedTreasureChestPresenter.TAG) as ViewCollectedTreasureChestPresenter
        else
            PresenterManager.addPresenter(ViewCollectedTreasureChestPresenter.TAG, ViewCollectedTreasureChestPresenter(CollectedTreasureChestConnectionImpl())) as ViewCollectedTreasureChestPresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)

        treasureChestImage = parent.view_treasure_chest_container_image

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

    private fun openTreasureChest() {
        viewCollectedTreasureChestPresenter.openTreasureChest()
    }
}