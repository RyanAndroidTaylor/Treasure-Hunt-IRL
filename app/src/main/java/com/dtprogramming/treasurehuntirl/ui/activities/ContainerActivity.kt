package com.dtprogramming.treasurehuntirl.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.os.Bundle
import android.support.v4.util.ArrayMap
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.dtprogramming.treasurehuntirl.ui.container.*
import com.dtprogramming.treasurehuntirl.util.ContainerAnimation
import java.util.*

/**
 * Created by ryantaylor on 7/4/16.
 */
abstract class ContainerActivity : BaseActivity() {
    val CURRENT_URI = "Current Uri"

    protected var currentRootView: View? = null
    protected var currentContainer: Container? = null

    protected val currentUri: String?
        get() {
            return peekBackStack()
        }

    //TODO Back stack not being saved if activity is destroyed
    private val backStack = Stack<String>()
    private val containerMap = ArrayMap<String, ContainerData>()

    private var duration = 300L
    private var currentInAnimation: ContainerAnimation<Any>? = null
    private var currentOutAnimation: ContainerAnimation<Any>? = null

    abstract var parent: ViewGroup

    override fun onSaveInstanceState(outState: Bundle?) {
        if (currentUri != null)
            outState?.putString(CURRENT_URI, currentUri)

        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()

        currentContainer?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) {
            for (i in 0..containerMap.size - 1) {
                containerMap.valueAt(i).container.onFinish()
            }
        }
    }

    abstract fun setToolBarTitle(title: String)

    fun setAnimations(inAnimation: ContainerAnimation<Any>?, outAnimation: ContainerAnimation<Any>?, duration: Long = 300): ContainerActivity {
        if (inAnimation != null && currentInAnimation != null)
            Log.e("ContainerActivity", "Setting currentInAnimation before the previous one was run")
        if (outAnimation != null && currentOutAnimation != null)
            Log.e("ContainerActivity", "Setting currentOutAnimation before the previous one was run")

        currentInAnimation = inAnimation
        currentOutAnimation = outAnimation

        this.duration = duration

        return this
    }

    fun startContainer(uri: String) {
        startContainer(uri, true)
    }

    fun startContainer(uri: String, addToBackStack: Boolean) {
        startContainer(uri, Bundle(), addToBackStack)
    }

    fun startContainer(uri: String, extras: Bundle) {
        startContainer(uri, extras, true)
    }

    fun startContainer(uri: String, extras: Bundle, addToBackStack: Boolean) {
        currentContainer?.onPause()

        loadContainer(uri, extras)

        if (addToBackStack)
            addToBackStack(uri)
    }

    protected fun loadCurrentContainer() {
        loadContainer(currentUri, Bundle())
    }

    private fun loadPreviousContainer() {
        currentContainer?.onFinish()

        popBackStack()

        val oldRootView = currentRootView

        val containerData = containerMap[currentUri]

        currentContainer = containerData?.container
        currentRootView = containerData?.rootView

        if (hasPendingAnimation() && parent.childCount > 0) {
            if (oldRootView != null && currentRootView != null) {
                runAnimation(oldRootView, currentRootView!!)
            }
        } else {
            parent.removeAllViews()

            parent.addView(currentRootView)
        }

        currentContainer?.onReload(parent)
    }

    private fun loadContainer(uri: String?, extras: Bundle) {
        currentContainer?.onPause()

        currentContainer = createContainer(uri)

        val oldContainerView = currentRootView
        currentRootView = currentContainer!!.inflate(this, parent, extras)

        containerMap.put(uri!!, ContainerData(currentContainer!!, currentRootView!!))

        if (hasPendingAnimation() && parent.childCount > 0) {

            runAnimation(oldContainerView!!, currentRootView!!)
        } else {
            if (parent.childCount > 0)
                parent.removeAllViews()

            parent.addView(currentRootView!!)
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
            SearchTreasureHuntContainer.URI -> container = SearchTreasureHuntContainer()
            PlayTreasureHuntListContainer.URI -> container = PlayTreasureHuntListContainer()
            CreateTreasureHuntListContainer.URI -> container = CreateTreasureHuntListContainer()
            else -> throw IllegalStateException("There was no match found for the URI: $uri")
        }

        return container
    }

    private fun hasPendingAnimation(): Boolean {
        return currentOutAnimation != null || currentInAnimation != null
    }

    private fun runAnimation(currentContainerView: View, newContainerView: View) {
        newContainerView.visibility = View.INVISIBLE

        parent.addView(newContainerView)

        val inAnimator = currentInAnimation?.getAnimator(newContainerView)
        val outAnimator = currentOutAnimation?.getAnimator(currentContainerView)

        val animatorSet = AnimatorSet()

        animatorSet.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)

                if (parent.childCount > 1)
                    parent.removeViewAt(0)

                currentInAnimation = null
                currentOutAnimation = null
            }
        })

        if (inAnimator != null) {
            if (outAnimator != null)
                animatorSet.play(inAnimator).with(outAnimator)
            else
                animatorSet.play(inAnimator)
        } else if (outAnimator != null) {
            animatorSet.play(outAnimator)
        }

        newContainerView.visibility = View.VISIBLE

        animatorSet.duration = duration
        animatorSet.start()
    }

    open protected fun addToBackStack(uri: String) {
        backStack.push(uri)
    }

    open protected fun popBackStack() {
        backStack.pop()
    }

    open protected fun peekBackStack(): String? {
        if (!backStack.isEmpty())
            return backStack.peek()
        else
            return null
    }

    open protected fun isEmptyBackStack(): Boolean {
        return backStack.isEmpty()
    }

    open protected fun backStackSize(): Int {
        return backStack.size
    }

    fun finishCurrentContainer() {
        if (backStackSize() > 1) {
            loadPreviousContainer()
        } else {
            finish()
        }
    }

    override fun onBackPressed() {
        if (backStackSize() > 1) {
            loadPreviousContainer()
        } else {
            super.onBackPressed()
        }
    }
}