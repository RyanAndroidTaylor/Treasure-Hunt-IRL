package com.dtprogramming.treasurehuntirl.ui.views

import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TextClue
import com.dtprogramming.treasurehuntirl.database.models.Waypoint

/**
 * Created by ryantaylor on 7/11/16.
 */
interface CreateTreasureChestView {

    fun updateClueList(clues: List<Clue>)
    fun displayWaypointInfo(waypoint: Waypoint?)
    fun hideWaypointInfo()
    fun displayPassPhraseInfo(passPhrase: String)
    fun hidePassPhraseInfo()
    fun setTitle(title: String)
    fun setState(state: Int)
}