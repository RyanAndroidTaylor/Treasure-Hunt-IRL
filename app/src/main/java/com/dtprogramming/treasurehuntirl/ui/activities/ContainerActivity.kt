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

    abstract fun setToolBarTitle(title: String)

    fun loadContainer(uri: String) {
        loadContainer(uri, Bundle())
    }

    fun loadContainer(uri: String, extras: Bundle) {
        InflateContainer(uri, extras)

        backStack.push(uri)
    }


    private fun loadCurrentContainer() {
        InflateContainer(currentUri, Bundle())
    }

    private fun InflateContainer(uri: String?, extras: Bundle) {
        if (containerMap.containsKey(uri)) {
            container = containerMap[uri]!!

            container?.reload(parent)
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
        container?.onFinish()

        if (backStack.size > 1) {
            containerMap.remove(currentUri)

            backStack.pop()

            loadCurrentContainer()
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        container?.onFinish()

        if (backStack.size > 1) {
            containerMap.remove(currentUri)

            backStack.pop()

            loadCurrentContainer()
        } else {
            super.onBackPressed()
        }
    }
}