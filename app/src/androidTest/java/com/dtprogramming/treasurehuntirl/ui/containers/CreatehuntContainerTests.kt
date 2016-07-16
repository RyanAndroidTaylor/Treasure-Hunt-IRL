package com.dtprogramming.treasurehuntirl.ui.containers

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToHolder
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.Matchers
import com.dtprogramming.treasurehuntirl.ui.activities.CreateHuntActivityTest
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ryantaylor on 6/16/16.
 */
@RunWith(AndroidJUnit4::class)
class CreateHuntContainerTests : CreateHuntActivityTest() {

    @Test
    fun shouldDisplayNewTitleWhenCreatingANewTreasureHunt() {
        launchNewHunt()

        onView(withId(R.id.create_hunt_container_title)).check(matches(withText("New Treasure Hunt")))
    }

    @Test
    fun shouldDisplayCreatedTitleWhenLoadingAnExistingTreasureHunt() {
        launchExistingHunt()

        onView(withId(R.id.create_hunt_container_title)).check(matches(withText(createdTitle)))
    }

    @Test
    fun shouldDisplayTreasureChestsSavedToThisTreasureHunt() {
        launchExistingHunt()

        onView(withId(R.id.create_hunt_container_chest_list)).perform(scrollToHolder(Matchers.withTreasureChestInfo(treasureChestTitleOne)))
        onView(withId(R.id.create_hunt_container_chest_list)).perform(scrollToHolder(Matchers.withTreasureChestInfo(treasureChestTitleTwo)))
    }

    @Test
    fun shouldNotDisplayTreasureChestNotSavedToTreasureHunt() {
        launchExistingHunt()

        var failedCount = 0
        onView(withId(R.id.create_hunt_container_chest_list)).withFailureHandler { error, viewMatcher -> failedCount++ }.perform(scrollToHolder(Matchers.withTreasureChestInfo(treasureChestTitleBad)))

        assertTrue(failedCount == 1)
    }

    @Test
    fun shouldLoadCreateTreasureChestContainerWithNewTreasureChestWhenFabIsClicked() {
        launchNewHunt()

        onView(withId(R.id.create_hunt_container_add_chest)).perform(click())

        onView(withId(R.id.create_chest_container_add_clue)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.create_chest_container_add_waypoint)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.create_chest_container_title)).check(ViewAssertions.matches(withText("New Treasure Chest")))
    }

    @Test
    fun shouldLoadCreateTreasureChestContainerWithRequestTreasureChestWhenTreasureChestIsClickedOn() {
        launchExistingHunt()

        onView(withId(R.id.create_hunt_container_chest_list)).perform(actionOnHolderItem(Matchers.withTreasureChestInfo(treasureChestTitleOne), click()))

        onView(withId(R.id.create_chest_container_clue_container)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.create_chest_container_title)).check(ViewAssertions.matches(withText(treasureChestTitleOne)))
    }
}