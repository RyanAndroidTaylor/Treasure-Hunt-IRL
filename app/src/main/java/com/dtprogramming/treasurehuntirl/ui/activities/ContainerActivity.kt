package com.dtprogramming.treasurehuntirl.ui.activities

import android.os.Bundle
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

    private val backStack = Stack<String>()
    private val containerMap = HashMap<String, Container>()

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
            for (container in containerMap.values) {
                container.onFinish()
            }
        }
    }

    abstract fun setToolBarTitle(title: String)

    fun startContainer(uri: String) {
        startContainer(uri, Bundle())
    }

    fun startContainer(uri: String, extras: Bundle) {
        container?.onPause()

        loadContainer(uri, extras)

        backStack.push(uri)
    }


    private fun loadCurrentContainer() {
        loadContainer(currentUri, Bundle())
    }

    private fun loadContainer(uri: String?, extras: Bundle) {
        if (containerMap.containsKey(uri)) {
            container = containerMap[uri]!!

            container?.onReload(parent)
        } else {
            when (uri) {
                CreateHuntContainer.URI -> container = CreateHuntContainer()
                CreateClueContainer.URI -> container = CreateClueContainer()
                CreateWayPointContainer.URI -> container = CreateWayPointContainer()
                CreateTreasureChestContainer.URI -> container = CreateTreasureChestContainer()
                else -> throw IllegalStateException("There was no match found for the URI: $uri")
            }

            containerMap.put(uri, container!!)

            container?.inflate(this, parent, extras)
        }
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