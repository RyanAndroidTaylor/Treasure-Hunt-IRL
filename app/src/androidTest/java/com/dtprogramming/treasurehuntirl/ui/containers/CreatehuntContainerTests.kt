package com.dtprogramming.treasurehuntirl.ui.containers

import android.content.Intent
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.espresso.assertion.ViewAssertions.*
import android.support.test.espresso.contrib.RecyclerViewActions.*
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.FailureHandler
import android.support.test.espresso.assertion.ViewAssertions
import android.view.View
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.ui.Matchers
import com.dtprogramming.treasurehuntirl.ui.activities.CreateHuntActivity
import com.dtprogramming.treasurehuntirl.util.HUNT_UUID
import com.dtprogramming.treasurehuntirl.util.NEW
import com.dtprogramming.treasurehuntirl.util.TREASURE_CHEST_UUID
import org.hamcrest.Matcher
import org.junit.After
import org.junit.Before

import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/**
 * Created by ryantaylor on 6/16/16.
 */
@RunWith(AndroidJUnit4::class)
class CreatehuntContainerTests {

    val treasureHuntUuid = "TestUuid"
    val createdTitle = "CreatedTitle"

    val treasureChestTitleOne = "Treasure chest title one"
    val treasureChestTitleTwo = "Treasure chest title two"

    val treasureChestTitleBad = "Treasure chest title bad"

    @get:Rule
    val activityTestRule: ActivityTestRule<CreateHuntActivity> = ActivityTestRule(CreateHuntActivity::class.java, true, false)

    @Before
    fun setUp() {

    }

    @After
    fun tearDown() {
        THApp.briteDatabase.delete(Clue.TABLE.NAME, null)
        THApp.briteDatabase.delete(TreasureHunt.TABLE.NAME, null)
        THApp.briteDatabase.delete(TreasureChest.TABLE.NAME, null)
    }

    @Test
    fun shouldDisplayNewTitleWhenCreatingANewTreasureHunt() {
        launchNewHunt()

        onView(withId(R.id.create_hunt_container_title)).check(matches(withText("New Treasure Hunt")))
    }

    @Test
    fun shouldDisplayCreatedTitleWhenLoadingAnExistingTreasureHunt() {
        launchActivityWithAlreadyCreatedHunt()

        onView(withId(R.id.create_hunt_container_title)).check(matches(withText(createdTitle)))
    }

    @Test
    fun shouldDisplayTreasureChestsSavedToThisTreasureHunt() {
        launchActivityWithAlreadyCreatedHunt()

        onView(withId(R.id.create_hunt_container_chest_list)).perform(scrollToHolder(Matchers.withTreasureChestInfo(treasureChestTitleOne)))
        onView(withId(R.id.create_hunt_container_chest_list)).perform(scrollToHolder(Matchers.withTreasureChestInfo(treasureChestTitleTwo)))
    }

    @Test
    fun shouldNotDisplayTreasureChestNotSavedToTreasureHunt() {
        launchActivityWithAlreadyCreatedHunt()

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
        launchActivityWithAlreadyCreatedHunt()

        onView(withId(R.id.create_hunt_container_chest_list)).perform(actionOnHolderItem(Matchers.withTreasureChestInfo(treasureChestTitleOne), click()))

        onView(withId(R.id.create_chest_container_add_clue)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.create_chest_container_add_waypoint)).check(ViewAssertions.matches(isDisplayed()))
        onView(withId(R.id.create_chest_container_title)).check(ViewAssertions.matches(withText(treasureChestTitleOne)))
    }

    private fun launchNewHunt() {
        val intent = Intent()

        intent.putExtra(NEW, true)

        activityTestRule.launchActivity(intent)
    }

    private fun launchActivityWithAlreadyCreatedHunt() {
        THApp.briteDatabase.insert(TreasureHunt.TABLE.NAME, TreasureHunt(treasureHuntUuid, createdTitle).getContentValues())

        THApp.briteDatabase.insert(TreasureChest.TABLE.NAME, TreasureChest("treasureChestUuid1", treasureHuntUuid, treasureChestTitleOne).getContentValues())
        THApp.briteDatabase.insert(TreasureChest.TABLE.NAME, TreasureChest("treasureChestUuid2", treasureHuntUuid, treasureChestTitleTwo).getContentValues())

        // A treasure chest that should not be displayed since it is saved to a different treasure hunt
        THApp.briteDatabase.insert(TreasureChest.TABLE.NAME, TreasureChest("treasureChestUuid3", "Not this hunts uuid", treasureChestTitleBad).getContentValues())

        val intent = Intent()

        intent.putExtra(HUNT_UUID, treasureHuntUuid)

        activityTestRule.launchActivity(intent)
    }
}