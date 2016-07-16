package com.dtprogramming.treasurehuntirl.ui

import android.util.Log
import android.view.View
import com.dtprogramming.treasurehuntirl.ui.recycler_view.ClueAdapter
import com.dtprogramming.treasurehuntirl.ui.recycler_view.TreasureChestAdapter
import com.dtprogramming.treasurehuntirl.ui.recycler_view.TreasureHuntAdapter
import com.dtprogramming.treasurehuntirl.ui.views.AdjustableValueView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Created by ryantaylor on 6/20/16.
 */
object Matchers {

    fun withAdjustableValueViewText(text: String): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun matchesSafely(item: View?): Boolean {
                Log.i("Matchers", "\n$text\n${(item as AdjustableValueView).mText.toString()}")
                return item != null && item is AdjustableValueView && item.mText.toString().equals(text)
            }

            override fun describeTo(description: Description?) {
                description?.appendText("No AdjustableValueView with text: $text")
            }
        }
    }

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

    fun withTreasureHuntInfo(expectedTitle: String, expectedTreasureChestCount: Int): Matcher<TreasureHuntAdapter.TreasureHuntViewHolder> {
        return object : TypeSafeMatcher<TreasureHuntAdapter.TreasureHuntViewHolder>() {
            override fun matchesSafely(item: TreasureHuntAdapter.TreasureHuntViewHolder?): Boolean {
                val expectedTreasureChestCountString = if (expectedTreasureChestCount == 1)
                    "$expectedTreasureChestCount Treasure Chest"
                else
                    "$expectedTreasureChestCount Treasure Chests"

                return item != null
                        && item.titleText.text.toString().equals(expectedTitle)
                        && item.chestCount.text.toString().equals(expectedTreasureChestCountString)
            }

            override fun describeTo(description: Description?) {
                description?.appendText("No view holder found with title: $expectedTitle, treasure chest count: $expectedTreasureChestCount")
            }

        }
    }

    fun withTreasureChestInfo(expectedTreasureChestTitle: String): Matcher<TreasureChestAdapter.TreasureChestViewHolder> {
        return object : TypeSafeMatcher<TreasureChestAdapter.TreasureChestViewHolder>() {
            override fun describeTo(description: Description?) {
                description?.appendText("No view holder found with title: $expectedTreasureChestTitle")
            }

            override fun matchesSafely(item: TreasureChestAdapter.TreasureChestViewHolder?): Boolean {
                return item != null && item.treasureChestTitle.text.toString().equals(expectedTreasureChestTitle)
            }
        }
    }
}