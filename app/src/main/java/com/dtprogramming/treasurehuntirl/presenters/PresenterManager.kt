package com.dtprogramming.treasurehuntirl.presenters

import java.util.*

/**
 * Created by ryantaylor on 6/22/16.
 */
object PresenterManager {

    private val presenters = HashMap<String, Presenter>()

    fun hasPresenter(tag: String): Boolean {
        return presenters.containsKey(tag)
    }

    fun addPresenter(tag: String, presenter: Presenter): Presenter {
        presenters.put(tag, presenter)

        return presenter
    }

    fun getPresenter(tag: String): Presenter {
       return presenters[tag]!!
    }

    fun removePresenter(tag: String) {
        if (presenters.containsKey(tag))
            presenters.remove(tag)
    }
}