package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.connections.impl.TreasureChestConnectionImpl
import com.dtprogramming.treasurehuntirl.database.connections.impl.TreasureHuntConnectionImpl
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.presenters.CreateHuntPresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.recycler_view.TreasureChestAdapter
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import com.dtprogramming.treasurehuntirl.util.HUNT_UUID
import com.dtprogramming.treasurehuntirl.util.NEW
import com.dtprogramming.treasurehuntirl.util.TREASURE_CHEST_UUID
import kotlinx.android.synthetic.main.container_create_hunt.view.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateHuntContainer() : BasicContainer(), CreateHuntView {

    companion object {
        val URI: String = CreateHuntContainer::class.java.simpleName
    }

    private val createHuntPresenter: CreateHuntPresenter

    private lateinit var treasureChestList: RecyclerView
    private lateinit var adapter: TreasureChestAdapter

    init {
        createHuntPresenter = if (PresenterManager.hasPresenter(CreateHuntPresenter.TAG))
            PresenterManager.getPresenter(CreateHuntPresenter.TAG) as CreateHuntPresenter
        else
            PresenterManager.addPresenter(CreateHuntPresenter.TAG, CreateHuntPresenter(TreasureHuntConnectionImpl(), TreasureChestConnectionImpl())) as CreateHuntPresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)
        inflateView(R.layout.container_create_hunt)

        parent.create_hunt_container_add_chest.setOnClickListener { loadCreateTreasureChestContainer() }

        treasureChestList = parent.create_hunt_container_chest_list

        treasureChestList.layoutManager = LinearLayoutManager(containerActivity)

        adapter = TreasureChestAdapter(loadTreasureChest, containerActivity, listOf())

        treasureChestList.adapter = adapter

        if (extras.containsKey(HUNT_UUID))
            createHuntPresenter.load(this, extras.getString(HUNT_UUID))
        else if (extras.containsKey(NEW))
            createHuntPresenter.create(this)
        else
            createHuntPresenter.reload(this)

        return this
    }

    val loadTreasureChest: (treasureChest: TreasureChest) -> Unit = {
        val extras = Bundle()

        extras.putString(TREASURE_CHEST_UUID, it.uuid)
        extras.putString(HUNT_UUID, it.treasureHuntId)

        containerActivity.loadContainer(CreateTreasureChestContainer.URI, extras)
    }

    override fun onTreasureChestsLoaded(treasureChests: List<TreasureChest>) {
        adapter.updateList(treasureChests)
    }

    fun loadCreateTreasureChestContainer() {
        val bundle = Bundle()

        bundle.putBoolean(NEW, true)
        bundle.putString(HUNT_UUID, createHuntPresenter.treasureHuntId)

        containerActivity.loadContainer(CreateTreasureChestContainer.URI, bundle)
    }

    override fun onFinish() {
        createHuntPresenter.finish()
    }
}