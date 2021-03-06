package com.dtprogramming.treasurehuntirl.ui.activities

import android.os.Bundle
import android.util.ArrayMap
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.container.*
import com.dtprogramming.treasurehuntirl.ui.views.BottomTab
import kotlinx.android.synthetic.main.activity_bottom_tab.*
import java.util.*

/**
 * Created by ryantaylor on 8/8/16.
 */
class BottomTabActivity : ContainerActivity() {
    private val SELECTED_CONTAINER_URI = "SelectedContainerUri"

    lateinit override var parent: ViewGroup

    private lateinit var bottomTab: BottomTab

    private var selectedTabContainerUri: String? = null

    private val backStacks = ArrayMap<String, Stack<String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_tab)
        setSupportActionBar(toolbar)

        parent = tab_view
        bottomTab = bottom_tab

        bottomTab.tabSelectedListener = {
            selectedTabContainerUri = it

            if (currentUri != null)
                loadCurrentContainer()
            else
                startContainer(it)
        }

        addTab(R.drawable.clue_icon_24dp, "Search", SearchTreasureHuntContainer.URI)
        addTab(R.drawable.play_icon_24dp, "Play", PlayTreasureHuntListContainer.URI)
        addTab(R.drawable.create_icon_24dp, "Create", CreateTreasureHuntListContainer.URI)

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
        toolbar.title = title
    }

    override fun addToBackStack(uri: String) {
        selectedTabContainerUri?.let {
            backStacks[it]?.push(uri)
        }
    }

    override fun peekBackStack(): String? {
        selectedTabContainerUri?.let {
            return backStacks[selectedTabContainerUri]?.peek()
        }

        return null
    }

    override fun popBackStack() {
        selectedTabContainerUri?.let {
            backStacks[it]?.pop()
        }
    }

    override fun isEmptyBackStack(): Boolean {
        selectedTabContainerUri?.let {
            val backStack = backStacks[it]

            return backStack != null && backStack.isEmpty()
        }

        return true
    }

    override fun backStackSize(): Int {
        var size = 0

        selectedTabContainerUri?.let { size = backStacks[it]!!.size }

        return size
    }

    private fun addTab(iconId: Int, title: String, containerUri: String) {
        bottomTab.addTab(BottomTab.Tab(resources.getDrawable(iconId), title, containerUri))

        val backStack = Stack<String>()

        backStack.push(containerUri)

        backStacks.put(containerUri, backStack)
    }
}