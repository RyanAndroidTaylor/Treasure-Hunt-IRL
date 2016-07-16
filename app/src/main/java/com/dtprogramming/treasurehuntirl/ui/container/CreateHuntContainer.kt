package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup
import android.widget.EditText
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

    private lateinit var treasureHuntTitle: EditText

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

        treasureHuntTitle = parent.create_hunt_container_title

        parent.create_hunt_container_add_chest.setOnClickListener { loadCreateTreasureChestContainer() }

        treasureChestList = parent.create_hunt_container_chest_list

        treasureChestList.layoutManager = LinearLayoutManager(containerActivity)

        adapter = TreasureChestAdapter(treasureChestSelectedListener, containerActivity, listOf())

        treasureChestList.adapter = adapter

        if (extras.containsKey(HUNT_UUID))
            createHuntPresenter.load(this, extras.getString(HUNT_UUID))
        else if (extras.containsKey(NEW))
            createHuntPresenter.create(this)
        else
            createHuntPresenter.reload(this)

        treasureHuntTitle.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                createHuntPresenter.onTitleChanged(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        return this
    }

    override fun onPause() {
        super.onPause()

        createHuntPresenter.unsubscribe()
    }

    override fun onReload(parent: ViewGroup) {
        super.onReload(parent)

        createHuntPresenter.reload(this)
    }

    override fun onFinish() {
        super.onFinish()

        createHuntPresenter.finish()
    }

    override fun setTitle(title: String) {
        treasureHuntTitle.setText(title)
    }

    override fun onTreasureChestsLoaded(treasureChests: List<TreasureChest>) {
        adapter.updateList(treasureChests)
    }

    val treasureChestSelectedListener: (treasureChest: TreasureChest) -> Unit = {
        val extras = Bundle()

        extras.putString(TREASURE_CHEST_UUID, it.uuid)
        extras.putString(HUNT_UUID, it.treasureHuntId)

        containerActivity.startContainer(CreateTreasureChestContainer.URI, extras)
    }

    fun loadCreateTreasureChestContainer() {
        val bundle = Bundle()

        bundle.putBoolean(NEW, true)
        bundle.putString(HUNT_UUID, createHuntPresenter.treasureHuntId)

        containerActivity.startContainer(CreateTreasureChestContainer.URI, bundle)
    }
}