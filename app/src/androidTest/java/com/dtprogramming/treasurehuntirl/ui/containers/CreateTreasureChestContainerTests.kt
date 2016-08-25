package com.dtprogramming.treasurehuntirl.ui.containers

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.runner.AndroidJUnit4
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.activities.CreateTreasureHuntActivityTest
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ryantaylor on 7/14/16.
 */
@RunWith(AndroidJUnit4::class)
class CreateTreasureChestContainerTests : CreateTreasureHuntActivityTest() {

    @Test
    fun shouldDisplayNewTitleAndNotHaveAWaypointOrClueWhenNewTreasureChestIsBeingCreated() {
        launchNewChestContainer()

        onView(withId(R.id.create_chest_container_title)).check(matches(withText("New Treasure Chest")))
        onView(withId(R.id.create_chest_container_add_clue)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.create_chest_container_add_waypoint)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayCreateTitleAndWaypointAndClueWhenLoadingAnExistingTreasureChest() {
        launchExistingChestContainer()

        onView(withId(R.id.create_chest_container_title)).check(matches(withText(treasureChestTitleOne)))
        onView(withId(R.id.create_chest_container_clue_container)).check(matches(isDisplayed()))
        onView(withContentDescription("Google Map")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldLoadCreateClueContainerWhenAddClueButtonIsPressed() {
        launchNewChestContainer()

        onView(withId(R.id.create_chest_container_add_clue)).perform(click())
        onView(withId(R.id.create_clue__container_clue_text)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldLoadCreateWaypointContainerWhenAddWaypointIsPressed() {
        launchNewChestContainer()

        onView(withId(R.id.create_chest_container_add_waypoint)).perform(click())
        onView(withId(R.id.create_waypoint_container_map_container)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldLoadCreateClueContainerWhenClueIsPressed() {
        launchExistingChestContainer()

        onView(withId(R.id.create_chest_container_clue_container)).perform(click())
        onView(withId(R.id.create_clue__container_clue_text)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldLoadCreateWaypointContainerWhenWaypointIsPressed() {
        //TODO Not sure how to click on a waypoint at the moment
    }
}