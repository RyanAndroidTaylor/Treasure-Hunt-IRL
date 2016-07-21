package com.dtprogramming.treasurehuntirl.ui.activities

import android.content.Intent
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnHolderItem
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureChest
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.Matchers
import com.dtprogramming.treasurehuntirl.util.HUNT_UUID
import com.dtprogramming.treasurehuntirl.util.NEW
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ryantaylor on 7/15/16.
 */
@RunWith(AndroidJUnit4::class)
open class CreateHuntActivityTest {

    val treasureHuntUuid = "TestUuid"
    val createdTitle = "CreatedTitle"

    val treasureChestUuid1 = "treasureChestUuid1"
    val treasureChestUuid2 = "treasureChestUuid2"
    val treasureChestUuidBad = "treasureChestUuidBad"

    val treasureChestTitleOne = "Treasure chest title one"
    val treasureChestTitleTwo = "Treasure chest title two"

    val treasureChestTitleBad = "Treasure chest title bad"

    val clueUuid = "ClueUuid"
    val clueText = "Clue text"

    val waypointUuid = "WaypointUuid"
    val lat = 10.1
    val lng = 23.42

    @get:Rule
    val activityTestRule: ActivityTestRule<CreateHuntActivity> = ActivityTestRule(CreateHuntActivity::class.java, true, false)

    @Before
    open fun setUp() {
        THApp.briteDatabase.delete(Clue.TABLE.NAME, null)
        THApp.briteDatabase.delete(TreasureHunt.TABLE.NAME, null)
        THApp.briteDatabase.delete(TreasureChest.TABLE.NAME, null)
        THApp.briteDatabase.delete(Waypoint.TABLE.NAME, null)
    }

    @After
    open fun tearDown() {
        THApp.briteDatabase.delete(Clue.TABLE.NAME, null)
        THApp.briteDatabase.delete(TreasureHunt.TABLE.NAME, null)
        THApp.briteDatabase.delete(TreasureChest.TABLE.NAME, null)
        THApp.briteDatabase.delete(Waypoint.TABLE.NAME, null)
    }

    @Test
    fun fakeTestMethod() {

    }

    // Launch new
    fun launchNewHunt() {
        val intent = Intent()

        intent.putExtra(NEW, true)

        activityTestRule.launchActivity(intent)
    }

    fun launchNewChestContainer() {
        launchNewHunt()

        onView(withId(R.id.create_hunt_container_add_chest)).perform(click())
    }

    fun launchNewClueContainer() {
        launchNewChestContainer()

        onView(withId(R.id.create_chest_container_add_clue)).perform(click())
    }

    fun launchNewWaypointContainer() {
        launchNewChestContainer()

        onView(withId(R.id.create_chest_container_add_waypoint)).perform(click())
    }

    // Launch with data
    fun launchExistingHunt() {
        loadHuntData()

        val intent = Intent()

        intent.putExtra(HUNT_UUID, treasureHuntUuid)

        activityTestRule.launchActivity(intent)
    }

    fun launchExistingChestContainer() {
        launchExistingHunt()

        onView(withId(R.id.create_hunt_container_chest_list)).perform(actionOnHolderItem(Matchers.withTreasureChestInfo(treasureChestTitleOne), click()))
    }

    fun launchExistingClueContainer() {
        launchExistingHunt()

        onView(withId(R.id.create_chest_container_clue_container)).perform(click())
    }

    //TODO As far as I know you can't testing google maps with espresso =(
//    fun launchExistingWaypointContainer() {
//        launchExistingHunt()
//
//        onView(withId(R.id.create_chest_container_map_container)).perform(click())
//    }

    protected fun loadHuntData() {
        THApp.briteDatabase.insert(TreasureHunt.TABLE.NAME, TreasureHunt(treasureHuntUuid, createdTitle, null).getContentValues())

        THApp.briteDatabase.insert(TreasureChest.TABLE.NAME, TreasureChest(treasureChestUuid1, treasureHuntUuid, treasureChestTitleOne).getContentValues())
        THApp.briteDatabase.insert(TreasureChest.TABLE.NAME, TreasureChest(treasureChestUuid2, treasureHuntUuid, treasureChestTitleTwo).getContentValues())

        // A treasure chest that should not be displayed since it is saved to a different treasure hunt
        THApp.briteDatabase.insert(TreasureChest.TABLE.NAME, TreasureChest(treasureChestUuidBad, "Not this hunts uuid", treasureChestTitleBad).getContentValues())


        THApp.briteDatabase.insert(Clue.TABLE.NAME, Clue(clueUuid, treasureChestUuid1, clueText).getContentValues())

        THApp.briteDatabase.insert(Waypoint.TABLE.NAME, Waypoint(waypointUuid, treasureChestUuid1, lat, lng).getContentValues())
    }
}