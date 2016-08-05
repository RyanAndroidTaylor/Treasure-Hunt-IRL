package com.dtprogramming.treasurehuntirl.ui.activities

import android.os.Bundle
import android.support.v4.util.ArrayMap
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.ui.container.*
import java.util.*

/**
 * Created by ryantaylor on 7/4/16.
 */
abstract class ContainerActivity : BaseActivity() {
    val CURRENT_URI = "Current Uri"

    protected var container: Container? = null

    private val currentUri: String?
        get() {
            return backStack.peek()
        }

    //TODO Back stack not being saved if activity is destroyed
    private val backStack = Stack<String>()
    private val containerMap = ArrayMap<String, Container>()

    abstract var parent: ViewGroup

    override fun onSaveInstanceState(outState: Bundle?) {
        if (currentUri != null)
            outState?.putString(CURRENT_URI, currentUri)

        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()

        container?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) {
            for (i in 0..containerMap.size -1) {
                containerMap.valueAt(i).onFinish()
            }
        }
    }

    abstract fun setToolBarTitle(title: String)

    fun startContainer(uri: String) {
        startContainer(uri, Bundle())
    }

    fun startContainer(uri: String, extras: Bundle) {
        container?.onPause()

        loadContainer(uri, extras, true)

        backStack.push(uri)
    }

    fun startContaienrAsPopup(uri: String) {
        startContainerAsPopup(uri, Bundle())
    }

    fun startContainerAsPopup(uri: String, extras: Bundle) {
        container?.onPause()

        loadContainer(uri, extras, false)
    }

    private fun loadCurrentContainer() {
        loadContainer(currentUri, Bundle(), true)
    }

    private fun loadContainer(uri: String?, extras: Bundle, replaceContainer: Boolean) {
        if (containerMap.containsKey(uri)) {
            container = containerMap[uri]!!

            if (replaceContainer && parent.childCount > 0)
                parent.removeAllViews()

            container?.onReload(parent)
        } else {
            container = createContainer(uri)

            containerMap.put(uri!!, container!!)

            if (replaceContainer && parent.childCount > 0)
                parent.removeAllViews()

            container?.inflate(this, parent, extras)
        }
    }

    private fun createContainer(uri: String?): Container {
        val container: Container

        when (uri) {
            CreateHuntContainer.URI -> container = CreateHuntContainer()
            CreateTextClueContainer.URI -> container = CreateTextClueContainer()
            CreateWayPointContainer.URI -> container = CreateWayPointContainer()
            CreateTreasureChestContainer.URI -> container = CreateTreasureChestContainer()
            ViewTreasureHuntContainer.URI -> container = ViewTreasureHuntContainer()
            PlayTreasureHuntContainer.URI -> container = PlayTreasureHuntContainer()
            DigModeContainer.URI -> container = DigModeContainer()
            ViewCollectedTreasureChestContainer.URI -> container = ViewCollectedTreasureChestContainer()
            else -> throw IllegalStateException("There was no match found for the URI: $uri")
        }

        return container
    }

    fun finishCurrentContainer() {
        if (backStack.size > 1) {
            container?.onFinish()

            containerMap.remove(currentUri)

            backStack.pop()

            loadCurrentContainer()
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        if (backStack.size > 1) {
            container?.onFinish()

            containerMap.remove(currentUri)

            backStack.pop()

            loadCurrentContainer()
        } else {
            super.onBackPressed()
        }
    }
}