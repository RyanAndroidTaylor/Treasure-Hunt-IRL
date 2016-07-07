package com.dtprogramming.treasurehuntirl.ui

import com.dtprogramming.treasurehuntirl.ui.recycler_view.ClueAdapter
import com.dtprogramming.treasurehuntirl.ui.recycler_view.TreasureHuntAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Created by ryantaylor on 6/20/16.
 */
object Matchers {

    fun withClueText(expectedClueText: String): Matcher<ClueAdapter.ClueViewHolder> {
        return object : TypeSafeMatcher<ClueAdapter.ClueViewHolder>() {
            override fun describeTo(description: Description?) {
                description?.appendText("Could not find a ClueViewHolder that had text that matched \"$expectedClueText\"")
            }

            override fun matchesSafely(item: ClueAdapter.ClueViewHolder?): Boolean {
                return item != null && item.clueText.text.toString().equals(expectedClueText)
            }
        }
    }

    fun withTreasureHuntInfo(expectedTitle: String, expectedWaypointCount: Int, expectedClueCount: Int): Matcher<TreasureHuntAdapter.TreasureHuntViewHolder> {
        return object : TypeSafeMatcher<TreasureHuntAdapter.TreasureHuntViewHolder>() {
            override fun matchesSafely(item: TreasureHuntAdapter.TreasureHuntViewHolder?): Boolean {
                return item != null
                        && item.titleText.text.toString().equals(expectedTitle)
                        && item.waypointCount.text.toString().equals("$expectedWaypointCount Waypoints")
                        && item.clueCount.text.toString().equals("$expectedClueCount Clues")
            }

            override fun describeTo(description: Description?) {
                description?.appendText("No view holder found with title: $expectedTitle, waypoint count: $expectedWaypointCount, clue count: $expectedClueCount")
            }

        }
    }
}