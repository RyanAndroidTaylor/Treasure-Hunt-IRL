package com.dtprogramming.treasurehuntirl.ui.containers

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.runner.AndroidJUnit4
import com.dtprogramming.treasurehuntirl.R
import com.dtprogramming.treasurehuntirl.ui.Actions
import com.dtprogramming.treasurehuntirl.ui.Matchers
import com.dtprogramming.treasurehuntirl.ui.activities.CreateHuntActivityTest
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by ryantaylor on 6/26/16.
 */
@RunWith(AndroidJUnit4::class)
class CreateWaypointContainerTest : CreateHuntActivityTest() {

    @Test
    fun testInitialLayout() {
        launchNewWaypointContainer()

        onView(withId(R.id.create_waypoint_container_adjust_lat)).check(matches(Matchers.withAdjustableValueViewText("0.000000")))
        onView(withId(R.id.create_waypoint_container_adjust_lng)).check(matches(Matchers.withAdjustableValueViewText("0.000000")))
    }

    @Test
    fun testIncreaseDecreaseLatitudeAndLongitude() {
        launchNewWaypointContainer()

        onView(withId(R.id.create_waypoint_container_adjust_lat)).check(matches(Matchers.withAdjustableValueViewText("0.000000")))
        onView(withId(R.id.create_waypoint_container_adjust_lng)).check(matches(Matchers.withAdjustableValueViewText("0.000000")))

        // Latitude
        onView(withId(R.id.create_waypoint_container_adjust_lat)).perform(Actions.clickCenterRight())
        onView(withId(R.id.create_waypoint_container_adjust_lat)).check(matches(Matchers.withAdjustableValueViewText("0.000488")))

        onView(withId(R.id.create_waypoint_container_adjust_lat)).perform(Actions.clickCenterLeft())
        onView(withId(R.id.create_waypoint_container_adjust_lat)).check(matches(Matchers.withAdjustableValueViewText("0.000000")))

        // Longitude
        onView(withId(R.id.create_waypoint_container_adjust_lng)).perform(Actions.clickCenterRight())
        onView(withId(R.id.create_waypoint_container_adjust_lng)).check(matches(Matchers.withAdjustableValueViewText("0.000488")))

        onView(withId(R.id.create_waypoint_container_adjust_lng)).perform(Actions.clickCenterLeft())
        onView(withId(R.id.create_waypoint_container_adjust_lng)).check(matches(Matchers.withAdjustableValueViewText("0.000000")))
    }
}