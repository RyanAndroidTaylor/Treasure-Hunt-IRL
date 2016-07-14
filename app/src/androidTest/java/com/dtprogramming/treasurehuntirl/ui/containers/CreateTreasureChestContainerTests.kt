package com.dtprogramming.treasurehuntirl.ui.containers

import android.content.Intent
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.Matchers
import com.dtprogramming.treasurehuntirl.ui.activities.CreateHuntActivity
import com.dtprogramming.treasurehuntirl.util.HUNT_UUID
import com.dtprogramming.treasurehuntirl.util.NEW
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ryantaylor on 7/14/16.
 */
@RunWith(AndroidJUnit4::class)
class CreateTreasureChestContainerTests {

    val treasureHuntUuid = "TestUuid"
    val createdTitle = "CreatedTitle"

    val treasureChestTitleOne = "Treasure chest title one"
    val treasureChestTitleTwo = "Treasure chest title two"

    @get:Rule
    val activityTestRule = ActivityTestRule<CreateHuntActivity>(CreateHuntActivity::class.java, true, false)

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {
        THApp.briteDatabase.delete(Clue.TABLE.NAME, null)
        THApp.briteDatabase.delete(TreasureHunt.TABLE.NAME, null)
        THApp.briteDatabase.delete(TreasureChest.TABLE.NAME, null)
        THApp.briteDatabase.delete(Waypoint.TABLE.NAME, null)
    }

    @Test
    fun shouldDisplayNewTitleAndNotHaveAWaypointOrClueWhenNewTreasureChestIsBeingCreated() {
        loadNewTreasureChest()

        onView(withId(R.id.create_chest_container_title)).check(matches(withText("New Treasure Chest")))
        onView(withId(R.id.create_chest_container_add_clue)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.create_chest_container_add_waypoint)).check(ViewAssertions.matches(isDisplayed()))
    }

    @Test
    fun shouldDisplayCreateTitleAndWaypointAndClueWhenLoadingAnExistingTreasureChest() {
        loadExistingTreasureChest()

        onView(withId(R.id.create_chest_container_title)).check(matches(withText(treasureChestTitleOne)))
        onView(withId(R.id.create_chest_container_clue_container)).check(matches(isDisplayed()))
        onView(withContentDescription("Google Map")).check(matches(isDisplayed()))
    }

    @Test
    fun shouldLoadCreateClueContainerWhenAddClueButtonIsPressed() {
        loadNewTreasureChest()

        onView(withId(R.id.create_chest_container_add_clue)).perform(click())
        onView(withId(R.id.create_clue__container_clue_text)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldLoadCreateWaypointContainerWhenAddWaypointIsPressed() {
        loadNewTreasureChest()

        onView(withId(R.id.create_chest_container_add_waypoint)).perform(click())
        onView(withId(R.id.create_waypoint_container_map_container)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldLoadCreateClueContainerWhenClueIsPressed() {
        loadExistingTreasureChest()

        onView(withId(R.id.create_chest_container_clue_container)).perform(click())
        onView(withId(R.id.create_clue__container_clue_text)).check(matches(isDisplayed()))
    }

    @Test
    fun shouldLoadCreateWaypointContainerWhenWaypointisPressed() {
        //TODO Not sure how to click on a waypoint at the moment
    }

    private fun loadNewTreasureChest() {
        val intent = Intent()

        intent.putExtra(NEW, true)

        activityTestRule.launchActivity(intent)

        onView(withId(R.id.create_hunt_container_add_chest)).perform(ViewActions.click())
    }

    private fun loadExistingTreasureChest() {
        THApp.briteDatabase.insert(TreasureHunt.TABLE.NAME, TreasureHunt(treasureHuntUuid, createdTitle).getContentValues())

        THApp.briteDatabase.insert(TreasureChest.TABLE.NAME, TreasureChest("treasureChestUuid1", treasureHuntUuid, treasureChestTitleOne).getContentValues())
        THApp.briteDatabase.insert(TreasureChest.TABLE.NAME, TreasureChest("treasureChestUuid2", treasureHuntUuid, treasureChestTitleTwo).getContentValues())


        THApp.briteDatabase.insert(Clue.TABLE.NAME, Clue("clueUuid", "treasureChestUuid1", "Clue text").getContentValues())

        THApp.briteDatabase.insert(Waypoint.TABLE.NAME, Waypoint("WaypointUuid", "treasureChestUuid1", 101.1, 101.1).getContentValues())

        val intent = Intent()

        intent.putExtra(HUNT_UUID, treasureHuntUuid)

        activityTestRule.launchActivity(intent)

        onView(withId(R.id.create_hunt_container_chest_list)).perform(RecyclerViewActions.actionOnHolderItem(Matchers.withTreasureChestInfo(treasureChestTitleOne), click()))
    }
}