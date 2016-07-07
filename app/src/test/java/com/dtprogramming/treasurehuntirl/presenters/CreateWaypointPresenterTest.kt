package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.BuildConfig
import com.dtprogramming.treasurehuntirl.MockitoMatchers
import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.connections.TreasureHuntConnection
import com.dtprogramming.treasurehuntirl.database.connections.WaypointConnection
import com.dtprogramming.treasurehuntirl.ui.views.CreateWaypointView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.zzf
import org.junit.After
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config

/**
 * Created by ryantaylor on 6/26/16.
 */
@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class)
class CreateWaypointPresenterTest {

    lateinit var waypointConnection: WaypointConnection

    lateinit var createWaypointPresenter: CreateWaypointPresenter
    lateinit var createWaypointView: CreateWaypointView

    @Before
    fun setUp() {
        waypointConnection = Mockito.mock(WaypointConnection::class.java)

        createWaypointPresenter = CreateWaypointPresenter(waypointConnection)
        createWaypointView = Mockito.mock(CreateWaypointView::class.java)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun testMapLoaded() {
        createWaypointPresenter.load(createWaypointView, "fake uuid")

        createWaypointPresenter.mapLoaded()

        Mockito.verify(createWaypointView).loadMarker(Mockito.anyString(), Mockito.anyDouble(), Mockito.anyDouble())
    }

    @Test
    fun testMapClicked() {
        createWaypointPresenter.load(createWaypointView, "fake uuid")

        createWaypointPresenter.mapClicked(100.0, 100.0)

        Mockito.verify(createWaypointView).markerMoved(100.0, 100.0)
    }

    @Test
    fun testNudgeMarker() {
        createWaypointPresenter.load(createWaypointView, "fake uuid")

        createWaypointPresenter.increaseLat()
        Mockito.verify(createWaypointView, Mockito.times(1)).markerMoved(Mockito.anyDouble(), Mockito.anyDouble())

        createWaypointPresenter.decreaseLat()
        Mockito.verify(createWaypointView, Mockito.times(2)).markerMoved(Mockito.anyDouble(), Mockito.anyDouble())

        createWaypointPresenter.increaseLng()
        Mockito.verify(createWaypointView, Mockito.times(3)).markerMoved(Mockito.anyDouble(), Mockito.anyDouble())

        createWaypointPresenter.decreaseLng()
        Mockito.verify(createWaypointView, Mockito.times(4)).markerMoved(Mockito.anyDouble(), Mockito.anyDouble())
    }

    @Test
    fun testSave() {
        createWaypointPresenter.load(createWaypointView, "fake uuid")

        createWaypointPresenter.save()

        Mockito.verify(waypointConnection).insert(MockitoMatchers.anyObject())
        Mockito.verify(createWaypointView).close()
    }

    @Test
    fun cancel() {
        createWaypointPresenter.load(createWaypointView, "fake uuid")

        createWaypointPresenter.cancel()

        Mockito.verify(createWaypointView).close()
    }
}