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

    lateinit var treasureHuntConnection: TreasureHuntConnection
    lateinit var clueConnection: ClueConnection
    lateinit var waypointConnection: WaypointConnection

    lateinit var createHuntPresenter: CreateHuntPresenter
    lateinit var createHuntView: CreateHuntView


    @Before
    fun setUp() {
        treasureHuntConnection = Mockito.mock(TreasureHuntConnection::class.java)
        clueConnection = Mockito.mock(ClueConnection::class.java)
        waypointConnection = Mockito.mock(WaypointConnection::class.java)

        createHuntPresenter = CreateHuntPresenter(treasureHuntConnection, clueConnection, waypointConnection)
        createHuntView = Mockito.mock(CreateHuntView::class.java)

        Mockito.`when`(treasureHuntConnection.getTreasureHunt(Mockito.anyString())).thenReturn(TreasureHunt("uuid", "title"))

        @Suppress("UNCHECKED_CAST")
        Mockito.`when`(clueConnection.getTreasureHuntCluesAsync(Mockito.anyString(), MockitoMatchers.anyObject())).then {
            (it.arguments[1] as (List<Clue>)-> Unit).invoke(listOf<Clue>())
        }

        @Suppress("UNCHECKED_CAST")
        Mockito.`when`(waypointConnection.getTreasureHuntWaypointsAsync(Mockito.anyString(), MockitoMatchers.anyObject())).then {
            (it.arguments[1] as (List<Waypoint>)-> Unit).invoke(listOf<Waypoint>())
        }
    }

    @After
    fun tearDown() {

    }

    @Test
    fun createHunt() {
        createHuntPresenter.createHunt(createHuntView)

        Mockito.verify(treasureHuntConnection).insert(MockitoMatchers.anyObject())
        Mockito.verify(createHuntView).setTitle(Mockito.anyString())
    }

    @Test
    fun testLoad() {

        createHuntPresenter.loadHunt("fake uuid", createHuntView)

        Mockito.verify(treasureHuntConnection).getTreasureHunt("fake uuid")

        Mockito.verify(createHuntView).setTitle("title")
        Mockito.verify(clueConnection).getTreasureHuntCluesAsync(Mockito.anyString(), MockitoMatchers.anyObject())
        Mockito.verify(createHuntView).updateClueList(MockitoMatchers.anyObject())
    }

    @Test
    fun testReload() {
        createHuntPresenter.loadHunt("fake uuid", createHuntView)
        createHuntPresenter.reloadHunt(createHuntView)

        Mockito.verify(treasureHuntConnection).getTreasureHunt("fake uuid")

        Mockito.verify(createHuntView, Mockito.times(2)).setTitle("title")
        Mockito.verify(clueConnection, Mockito.times(2)).getTreasureHuntCluesAsync(Mockito.anyString(), MockitoMatchers.anyObject())
        Mockito.verify(createHuntView, Mockito.times(2)).updateClueList(MockitoMatchers.anyObject())
    }

    @Test
    fun testMapLoaded() {
        createHuntPresenter.loadHunt("fake uuid", createHuntView)

        createHuntPresenter.mapLoaded()

        Mockito.verify(createHuntView).updateWaypoints(MockitoMatchers.anyObject())
    }

    @Test
    fun testFinish() {
        createHuntPresenter.loadHunt("fake uuid", createHuntView)

        createHuntPresenter.finish()

        Mockito.verify(treasureHuntConnection).update(MockitoMatchers.anyObject())
        Mockito.verify(treasureHuntConnection).unsubscribe()
        Mockito.verify(clueConnection).unsubscribe()
        Mockito.verify(waypointConnection).unsubscribe()
    }
}