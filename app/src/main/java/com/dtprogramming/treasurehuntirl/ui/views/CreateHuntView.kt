package com.dtprogramming.treasurehuntirl.ui.views

import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.container.Container

/**
 * Created by ryantaylor on 6/20/16.
 */
interface CreateHuntView {
    fun updateClueList(clues: List<Clue>)
    fun updateWaypoints(waypoints: List<Waypoint>)

    fun error(message: String)

    fun finish()
}