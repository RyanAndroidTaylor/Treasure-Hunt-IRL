package com.dtprogramming.treasurehuntirl.ui.activities

import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.container.*
import com.dtprogramming.treasurehuntirl.ui.views.BottomTab
import kotlinx.android.synthetic.main.activity_bottom_tab.*

/**
 * Created by ryantaylor on 8/8/16.
 */
class BottomTabActivity : ContainerActivity() {
    private val SELECTED_CONTAINER_URI = "SelectedContainerUri"

    lateinit override var parent: ViewGroup

    private lateinit var bottomTab: BottomTab

    private var selectedTabContainerUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_tab)
        setSupportActionBar(toolbar)

        parent = tab_view
        bottomTab = bottom_tab

        bottomTab.tabSelectedListener = {
            selectedTabContainerUri = it

            startContainer(it, false)
        }

        bottomTab.addTab(BottomTab.Tab(resources.getDrawable(R.drawable.clue_icon_24dp), "Search", SearchTreasureHuntContainer.URI))
        bottomTab.addTab(BottomTab.Tab(resources.getDrawable(R.drawable.play_icon_24dp), "Play", PlayTreasureHuntListContainer.URI))
        bottomTab.addTab(BottomTab.Tab(resources.getDrawable(R.drawable.create_icon_24dp), "Create", CreateTreasureHuntListContainer.URI))

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_CONTAINER_URI))
            bottomTab.moveToTab(savedInstanceState.getString(SELECTED_CONTAINER_URI))
    }

    override fun onResume() {
        super.onResume()

        selectedTabContainerUri?.let { bottomTab.moveToTab(it) }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (outState != null && selectedTabContainerUri != null)
            outState.putString(SELECTED_CONTAINER_URI, selectedTabContainerUri)

        super.onSaveInstanceState(outState)
    }

    override fun setToolBarTitle(title: String) {
        //TODO Set title
    }
}