package com.dtprogramming.treasurehuntirl.ui.activities

import android.os.Bundle
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.ui.container.Container
import com.dtprogramming.treasurehuntirl.ui.container.CreateClueContainer
import com.dtprogramming.treasurehuntirl.ui.container.CreateHuntContainer
import com.dtprogramming.treasurehuntirl.ui.container.CreateWayPointContainer
import java.util.*

/**
 * Created by ryantaylor on 7/4/16.
 */
abstract class ContainerActivity : BaseActivity() {
    val CURRENT_URI = "Current Uri"

    protected var container: Container? = null

    private val currentUri: String?
        get() { return backStack.peek() }

    private val backStack = Stack<String>()

    abstract var parent: ViewGroup

    override fun onSaveInstanceState(outState: Bundle?) {
        if (currentUri != null)
            outState?.putString(CURRENT_URI, currentUri)

        super.onSaveInstanceState(outState)
    }

    fun loadContainer(uri: String) {
        loadContainer(uri, Bundle())
    }

    fun loadContainer(uri: String, extras: Bundle) {
        when (uri) {
            CreateHuntContainer.URI -> container = CreateHuntContainer()
            CreateClueContainer.URI -> container = CreateClueContainer()
            CreateWayPointContainer.URI -> container = CreateWayPointContainer()
        }

        if (container != null) {
            container!!.inflate(this, parent, extras)
        } else {
            throw IllegalStateException("There was no match found for the URI: $uri")
        }

        backStack.push(uri)
    }


    private fun loadCurrentContainer() {
        when (currentUri) {
            CreateHuntContainer.URI -> container = CreateHuntContainer()
            CreateClueContainer.URI -> container = CreateClueContainer()
            CreateWayPointContainer.URI -> container = CreateWayPointContainer()
        }

        if (container != null) {
            container!!.inflate(this, parent, Bundle())
        } else {
            throw IllegalStateException("There was no match found for the URI: $currentUri")
        }
    }

    fun finishCurrentContainer() {
        container?.finish()

        if (backStack.size > 1) {

            backStack.pop()

            loadCurrentContainer()
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        container?.finish()

        if (backStack.size > 1) {
            backStack.pop()

            loadCurrentContainer()
        } else {
            super.onBackPressed()
        }
    }
}