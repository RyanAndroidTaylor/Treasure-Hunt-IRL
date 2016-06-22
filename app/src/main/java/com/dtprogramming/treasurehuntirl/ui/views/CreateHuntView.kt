package com.dtprogramming.treasurehuntirl.ui.views

/**
 * Created by ryantaylor on 6/20/16.
 */
interface CreateHuntView {

    fun loadCreateHuntContainer(clues: List<String>)
    fun loadCreateClueContainer()

    fun updateClueList(clues: List<String>)
}