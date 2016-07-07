package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.BuildConfig
import com.dtprogramming.treasurehuntirl.MockitoMatchers
import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.ui.views.CreateClueView
import org.junit.After
import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.annotation.Config

/**
 * Created by ryantaylor on 7/7/16.
 */
@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class)
class CreateCluePresenterTest {

    lateinit var clueConnection: ClueConnection

    lateinit var createClueView: CreateClueView
    lateinit var createCluePresenter: CreateCluePresenter

    @Before
    fun setUp() {
        clueConnection = Mockito.mock(ClueConnection::class.java)

        createClueView = Mockito.mock(CreateClueView::class.java)

        createCluePresenter = CreateCluePresenter(clueConnection)

        createCluePresenter.load(createClueView, "treasure hunt uuid")
    }

    @After
    fun tearDown() {

    }

    @Test
    fun testSave() {
        createCluePresenter.save()

        Mockito.verify(createClueView).close()
        Mockito.verify(clueConnection).insert(MockitoMatchers.anyObject())
    }

    @Test
    fun testCancel() {
        createCluePresenter.cancel()

        Mockito.verify(createClueView).close()
    }
}