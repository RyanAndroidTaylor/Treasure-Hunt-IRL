package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.PlayingTreasureHunt

/**
 * Created by ryantaylor on 7/20/16.
 */
interface PlayingTreasureHuntConnection : Connection{

    fun insert(playingTreasureHunt: PlayingTreasureHunt)

    fun getPlayingTreasureHuntsAsync(onComplete: (List<PlayingTreasureHunt>) -> Unit)
}