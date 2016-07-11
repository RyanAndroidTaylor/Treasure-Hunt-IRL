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
        container = getContainerForUri(uri)

        container?.inflate(this, parent, extras)

        backStack.push(uri)
    }


    private fun loadCurrentContainer() {
        container = getContainerForUri(currentUri)

        container?.inflate(this, parent, Bundle())
    }

    private fun getContainerForUri(uri: String?): Container {
        when (uri) {
            CreateHuntContainer.URI -> return CreateHuntContainer()
            CreateClueContainer.URI -> return CreateClueContainer()
            CreateWayPointContainer.URI -> return CreateWayPointContainer()
            CreateAnswerContainer.URI -> return CreateAnswerContainer()
            CreateTreasureChestContainer.URI -> return CreateTreasureChestContainer()
            else -> throw IllegalStateException("There was no match found for the URI: $uri")
        }
    }

    fun finishCurrentContainer() {
        container?.onFinish()

        if (backStack.size > 1) {

            backStack.pop()

            loadCurrentContainer()
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        container?.onFinish()

        if (backStack.size > 1) {
            backStack.pop()

            loadCurrentContainer()
        } else {
            super.onBackPressed()
        }
    }
}