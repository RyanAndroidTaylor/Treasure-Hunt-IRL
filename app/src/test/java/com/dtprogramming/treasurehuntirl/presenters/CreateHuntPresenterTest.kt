package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.BuildConfig
import com.dtprogramming.treasurehuntirl.MockitoMatchers
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.database.models.TreasureHunt
import com.dtprogramming.treasurehuntirl.database.models.Waypoint
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config

/**
 * Created by ryantaylor on 6/22/16.
 */
@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class)
class CreateHuntPresenterTest {

    lateinit var createHuntPresenter: CreateHuntPresenter
    lateinit var createHuntView: CreateHuntView


    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {

    }
}