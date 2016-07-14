package com.dtprogramming.treasurehuntirl.ui.containers

import android.content.Intent
import android.support.test.espresso.Espresso.*
import android.support.test.espresso.action.ViewActions.*
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
import org.junit.runner.RunWith
import javax.annotation.Generated

/**
 * Created by ryantaylor on 7/14/16.
 */
@RunWith(AndroidJUnit4::class)
class CreateClueContainerTests {

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

    private fun loadNewTreasureChest() {
        val intent = Intent()

        intent.putExtra(NEW, true)

        activityTestRule.launchActivity(intent)

        onView(withId(R.id.create_hunt_container_add_chest)).perform(click())
        onView(withId(R.id.create_chest_container_add_clue)).perform(click())
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
        onView(withId(R.id.create_chest_container_clue_container)).perform(click())
    }
}