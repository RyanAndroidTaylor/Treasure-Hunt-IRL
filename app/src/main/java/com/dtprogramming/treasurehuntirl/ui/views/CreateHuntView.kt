package com.dtprogramming.treasurehuntirl.ui.views

import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.container.Container

/**
 * Created by ryantaylor on 6/20/16.
 */
interface CreateHuntView {

    fun initLoad()
    fun moveToContainer(container: Container)

    fun updateClueList(clues: List<String>)
    fun updateWaypoints(waypoints: List<Waypoint>)
}