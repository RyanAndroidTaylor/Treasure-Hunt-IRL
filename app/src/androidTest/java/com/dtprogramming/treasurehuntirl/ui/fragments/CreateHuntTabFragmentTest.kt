package com.dtprogramming.treasurehuntirl.ui.fragments

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.swipeLeft
import android.support.test.espresso.assertion.ViewAssertions
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.espresso.contrib.RecyclerViewActions.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.Matchers
import com.dtprogramming.treasurehuntirl.ui.activities.TreasureTabActivity
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ryantaylor on 6/15/16.
 */
@RunWith(AndroidJUnit4::class)
class CreateHuntTabFragmentTest {

    val treasureHuntUuid = "TestUuid"
    val createdTitle = "CreatedTitle"

    val treasureChestUuid1 = "treasureChestUuid1"
    val treasureChestUuid2 = "treasureChestUuid2"

    val treasureChestTitleOne = "Treasure chest title one"
    val treasureChestTitleTwo = "Treasure chest title two"

    @get:Rule
    val activityTestRule: ActivityTestRule<TreasureTabActivity> = ActivityTestRule(TreasureTabActivity::class.java)

    @Before
    fun setUp() {
        THApp.briteDatabase.insert(TreasureHunt.TABLE.NAME, TreasureHunt(treasureHuntUuid, createdTitle).getContentValues())

        THApp.briteDatabase.insert(TreasureChest.TABLE.NAME, TreasureChest(treasureChestUuid1, treasureHuntUuid, treasureChestTitleOne).getContentValues())
        THApp.briteDatabase.insert(TreasureChest.TABLE.NAME, TreasureChest(treasureChestUuid2, treasureHuntUuid, treasureChestTitleTwo).getContentValues())

        onView(withId(R.id.drawer_layout)).perform(swipeLeft())
        onView(withId(R.id.drawer_layout)).perform(swipeLeft())
    }

    @After
    fun tearDown() {
        THApp.briteDatabase.delete(Clue.TABLE.NAME, null)
        THApp.briteDatabase.delete(TreasureHunt.TABLE.NAME, null)
        THApp.briteDatabase.delete(TreasureChest.TABLE.NAME, null)
        THApp.briteDatabase.delete(Waypoint.TABLE.NAME, null)
    }

    @Test
    fun whenFABPressedOpenCreateHuntActivity() {
        onView(withId(R.id.create_hunt_fragment_fab)).perform(click())

        onView(withId(R.id.create_hunt_container_title)).check(ViewAssertions.matches(withText("New Treasure Hunt")))
    }

    @Test
    fun displaySavedHuntInfo() {
        onView(withId(R.id.create_hunt_list)).perform(scrollToHolder(Matchers.withTreasureHuntInfo(createdTitle, 2)))
    }

    @Test
    fun openSavedHunt() {
        onView(withId(R.id.create_hunt_list)).perform(actionOnHolderItem(Matchers.withTreasureHuntInfo(createdTitle, 2), click()))

        onView(withId(R.id.create_hunt_container_title)).check(ViewAssertions.matches(withText(createdTitle)))
    }
}