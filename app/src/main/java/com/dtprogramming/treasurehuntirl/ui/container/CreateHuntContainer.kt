package com.dtprogramming.treasurehuntirl.ui.container

import android.os.Bundle
import android.support.v7.widget.CardView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.connections.impl.ClueConnectionImpl
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
import com.dtprogramming.treasurehuntirl.util.PARENT_UUID
import com.dtprogramming.treasurehuntirl.util.TREASURE_CHEST_UUID
import kotlinx.android.synthetic.main.container_create_hunt.view.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateHuntContainer() : BasicContainer(), CreateHuntView {

    companion object {
        val URI: String = CreateHuntContainer::class.java.simpleName
    }

    override var rootViewId = R.layout.container_create_hunt

    private val createHuntPresenter: CreateHuntPresenter

    private lateinit var treasureHuntTitle: EditText

    private lateinit var treasureChestList: RecyclerView
    private lateinit var adapter: TreasureChestAdapter

    private lateinit var clueContainer: CardView
    private lateinit var clueText: TextView
    private lateinit var addClue: Button

    init {
        createHuntPresenter = if (PresenterManager.hasPresenter(CreateHuntPresenter.TAG))
            PresenterManager.getPresenter(CreateHuntPresenter.TAG) as CreateHuntPresenter
        else
            PresenterManager.addPresenter(CreateHuntPresenter.TAG, CreateHuntPresenter(TreasureHuntConnectionImpl(), TreasureChestConnectionImpl(), ClueConnectionImpl())) as CreateHuntPresenter
    }

    override fun inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): Container {
        super.inflate(containerActivity, parent, extras)

        treasureHuntTitle = parent.create_hunt_container_title
        clueContainer = parent.create_hunt_container_clue_container
        clueText = parent.create_hunt_container_clue_text
        addClue = parent.create_hunt_container_add_clue

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

        addClue.setOnClickListener { startCreateClueContainer() }

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

    override fun displayClue(text: String) {
        clueContainer.visibility = View.VISIBLE
        clueText.text = text

        addClue.visibility = View.GONE
    }

    val treasureChestSelectedListener: (treasureChest: TreasureChest) -> Unit = {
        val extras = Bundle()

        extras.putString(TREASURE_CHEST_UUID, it.uuid)
        extras.putString(HUNT_UUID, it.treasureHuntUuid)

        containerActivity.startContainer(CreateTreasureChestContainer.URI, extras)
    }

    fun loadCreateTreasureChestContainer() {
        val bundle = Bundle()

        bundle.putBoolean(NEW, true)
        bundle.putString(HUNT_UUID, createHuntPresenter.treasureHuntUuid)

        containerActivity.startContainer(CreateTreasureChestContainer.URI, bundle)
    }

    private fun startCreateClueContainer() {
        val extras = Bundle()

        extras.putString(PARENT_UUID, createHuntPresenter.treasureHuntUuid)

        containerActivity.startContainer(CreateClueContainer.URI, extras)
    }
}