package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.BuildConfig
import com.dtprogramming.treasurehuntirl.ui.container.Container
import com.dtprogramming.treasurehuntirl.ui.container.CreateClueContainer
import com.dtprogramming.treasurehuntirl.ui.container.CreateHuntContainer
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import org.junit.After
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
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
        createHuntPresenter = CreateHuntPresenter()
        createHuntView = Mockito.mock(CreateHuntView::class.java)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun testLoad() {
        createHuntPresenter.load("fake uuid", createHuntView)

        Mockito.verify(createHuntView).initLoad(Mockito.anyListOf(String::class.java))
    }

    @Test
    fun testReload() {
        createHuntPresenter.load("fake uuid", createHuntView)

        Mockito.verify(createHuntView).initLoad(Mockito.anyListOf(String::class.java))

        createHuntPresenter.reload(createHuntView)

        Mockito.verify(createHuntView, Mockito.atLeastOnce()).moveToContainer(Mockito.any(CreateHuntContainer::class.java))
    }

    @Test
    fun testSwitchState() {
        createHuntPresenter.load("Fake uuid", createHuntView)

        Mockito.verify(createHuntView).initLoad(Mockito.anyListOf(String::class.java))

        createHuntPresenter.switchState(CreateHuntPresenter.CREATE_CLUE)

        Mockito.verify(createHuntView, Mockito.atLeastOnce()).moveToContainer(Mockito.any(CreateClueContainer::class.java))

        createHuntPresenter.switchState(CreateHuntPresenter.CREATE_HUNT)

        Mockito.verify(createHuntView, Mockito.atLeastOnce()).moveToContainer(Mockito.any(CreateHuntContainer::class.java))
    }

    @Test
    fun testSaveClue() {
        createHuntPresenter.load("Some fake ID boi", createHuntView)

        createHuntPresenter.saveClue("some text")
    }
}