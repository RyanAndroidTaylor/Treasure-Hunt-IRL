package com.dtprogramming.treasurehuntirl.ui.views

/**
 * Created by ryantaylor on 6/20/16.
 */
interface CreateHuntView {

    fun initLoad(clues: List<String>)
    fun loadCreateHuntContainer(clues: List<String>)
    fun loadCreateClueContainer()
    fun loadCreateWayPointContainer()

    fun updateClueList(clues: List<String>)
}