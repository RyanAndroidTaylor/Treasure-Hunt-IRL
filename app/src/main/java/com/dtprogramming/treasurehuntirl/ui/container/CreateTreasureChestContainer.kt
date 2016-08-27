package com.dtprogramming.treasurehuntirl.ui.container

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.database.connections.impl.ClueConnectionImpl
import com.dtprogramming.treasurehuntirl.database.connections.impl.PassPhraseConnectionImpl
import com.dtprogramming.treasurehuntirl.database.connections.impl.TreasureChestConnectionImpl
import com.dtprogramming.treasurehuntirl.database.connections.impl.WaypointConnectionImpl
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.InventoryItem
import com.dtprogramming.treasurehuntirl.database.models.TextClue
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.presenters.CreateTreasureChestPresenter
import com.dtprogramming.treasurehuntirl.presenters.PresenterManager
import com.dtprogramming.treasurehuntirl.ui.activities.ContainerActivity
import com.dtprogramming.treasurehuntirl.ui.container.animation.inLeft
import com.dtprogramming.treasurehuntirl.ui.container.animation.outRight
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ClueScrollListener
import com.dtprogramming.treasurehuntirl.ui.recycler_view.CustomLinearLayoutManager
import com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter.ClueAdapter
import com.dtprogramming.treasurehuntirl.ui.views.CreateTreasureChestView
import com.dtprogramming.treasurehuntirl.util.*
import kotlinx.android.synthetic.main.container_create_treasure_chest.view.*

/**
 * Created by ryantaylor on 7/11/16.
 */
class CreateTreasureChestContainer : BaseContainer(), CreateTreasureChestView {

    companion object {
        val URI: String = CreateTreasureChestContainer::class.java.simpleName

        fun createTreasureChestBundle(treasureHuntUuid: String): Bundle {
            val extras = Bundle()

            extras.putBoolean(NEW, true)
            extras.putString(TREASURE_HUNT_UUID, treasureHuntUuid)

            return extras
        }

        fun loadTreasureChest(treasureChestUuid: String): Bundle {
            val extras = Bundle()

            extras.putString(TREASURE_CHEST_UUID, treasureChestUuid)

            return extras
        }
    }

    override var rootViewId = R.layout.container_create_treasure_chest

    private var createTreasureChestPresenter: CreateTreasureChestPresenter

    private lateinit var editTitle: EditText

    private lateinit var stateGroup: RadioGroup

    private lateinit var clueList: RecyclerView
    private lateinit var adapter: ClueAdapter

    private lateinit var addClue: TextView

    private lateinit var waypointContainer: FrameLayout
    private lateinit var editWaypoint: TextView
    private lateinit var waypointLat: TextView
    private lateinit var waypointLng: TextView

    private lateinit var passPhraseContainer: FrameLayout
    private lateinit var editPassPhrase: EditText

    init {
        createTreasureChestPresenter = if (PresenterManager.hasPresenter(CreateTreasureChestPresenter.TAG))
            PresenterManager.getPresenter(CreateTreasureChestPresenter.TAG) as CreateTreasureChestPresenter
        else
            PresenterManager.addPresenter(CreateTreasureChestPresenter.TAG, CreateTreasureChestPresenter()) as CreateTreasureChestPresenter
    }

    override fun  inflate(containerActivity: ContainerActivity, parent: ViewGroup, extras: Bundle): View {
        val view = super.inflate(containerActivity, parent, extras)
        containerActivity.setToolBarTitle(containerActivity.stringFrom(R.string.treasure_chest_action_bar_title))

        checkForLocationPermission()

        editTitle = view.create_chest_container_title

        stateGroup = view.create_chest_container_state_group

        waypointContainer = view.create_chest_container_waypoint_container
        editWaypoint = view.create_chest_container_edit_waypoint
        waypointLat = view.create_chest_container_lat
        waypointLng = view.create_chest_container_lng

        passPhraseContainer = view.create_chest_container_pass_phrase_container
        editPassPhrase = view.create_chest_container_pass_phrase

        addClue = view.create_chest_container_add_clue

        clueList = view.create_treasure_chest_clue_list

        adapter = ClueAdapter(listOf(), { itemSelected(it) })

        clueList.layoutManager = CustomLinearLayoutManager(containerActivity)
        clueList.addOnScrollListener(ClueScrollListener())
        clueList.adapter = adapter

        view.create_chest_container_title.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                createTreasureChestPresenter.titleChanged(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }
            override fun afterTextChanged(s: Editable?) { }
        })

        editPassPhrase.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                createTreasureChestPresenter.passPhraseChanged(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        stateGroup.setOnCheckedChangeListener { radioGroup, buttonId ->
            when (buttonId) {
                R.id.create_chest_container_state_buried -> createTreasureChestPresenter.stateChanged(BURIED)
                R.id.create_chest_container_state_locked -> createTreasureChestPresenter.stateChanged(LOCKED)
                R.id.create_chest_container_state_both -> createTreasureChestPresenter.stateChanged(BURIED_LOCKED)
            }
        }

        addClue.setOnClickListener { loadNewCreateClueContainer() }

        editWaypoint.setOnClickListener { moveToContainer(CreateWayPointContainer.URI) }

        loadPresenter(extras)

        return view
    }

    override fun onReload(parent: ViewGroup) {
        super.onReload(parent)

        createTreasureChestPresenter.reload(this)
    }

    override fun onPause() {
        super.onPause()

        createTreasureChestPresenter.unsubscribe()
    }

    override fun onFinish() {
        super.onFinish()

        containerActivity.setAnimations(inLeft(containerActivity), outRight(containerActivity))

        createTreasureChestPresenter.dispose()
    }

    override fun updateClueList(clues: List<Clue>) {
        adapter.updateList(clues)
    }

    override fun displayWaypointInfo(waypoint: Waypoint?) {
        waypointContainer.visibility = View.VISIBLE

        if (waypoint != null) {
            waypointLat.text = "${waypoint.lat.format(6)}"
            waypointLng.text = "${waypoint.long.format(6)}"

            editWaypoint.text = containerActivity.stringFrom(R.string.edit_waypoint)
        } else {
            editWaypoint.text = containerActivity.stringFrom(R.string.add_waypoint)
        }
    }

    override fun hideWaypointInfo() {
        waypointContainer.visibility = View.GONE
    }

    override fun displayPassPhraseInfo(passPhrase: String?) {
        editPassPhrase.setText(passPhrase)

        passPhraseContainer.visibility = View.VISIBLE
    }

    override fun hidePassPhraseInfo() {
        passPhraseContainer.visibility = View.GONE
    }

    override fun setTitle(title: String) {
        editTitle.setText(title)
    }

    override fun setState(state: Int) {
        when (state) {
            BURIED -> stateGroup.check(R.id.create_chest_container_state_buried)
            LOCKED -> stateGroup.check(R.id.create_chest_container_state_locked)
            BURIED_LOCKED -> stateGroup.check(R.id.create_chest_container_state_both)
        }
    }

    private fun loadPresenter(extras: Bundle) {
        if (extras.containsKey(TREASURE_CHEST_UUID))
            createTreasureChestPresenter.load(extras.getString(TREASURE_CHEST_UUID), this)
        else if (extras.containsKey(NEW)) {
            createTreasureChestPresenter.create(extras.getString(TREASURE_HUNT_UUID), this)
        } else
            createTreasureChestPresenter.reload(this)
    }

    private fun checkForLocationPermission() {
        if (ContextCompat.checkSelfPermission(containerActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(containerActivity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun moveToContainer(uri: String) {
        val extras = Bundle()

        extras.putString(PARENT_UUID, createTreasureChestPresenter.treasureChestUuid)

        containerActivity.startContainer(uri, extras)
    }

    private fun loadNewCreateClueContainer() {
        CreateTextClueContainer.startNewContainer(containerActivity, createTreasureChestPresenter.treasureChestUuid)
    }

    private fun itemSelected(item: InventoryItem) {
        when (item.type) {
            TEXT_CLUE -> {
                val textClue = item as TextClue

                CreateTextClueContainer.loadContainer(containerActivity, textClue.parentUuid, textClue.uuid)
            }
        }
    }
}