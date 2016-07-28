package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.BuildConfig
import com.dtprogramming.treasurehuntirl.MockitoMatchers
import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.models.TextClue
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
class CreateTextCluePresenterTest {

    lateinit var clueConnection: ClueConnection

    lateinit var createClueView: CreateClueView
    lateinit var createCluePresenter: CreateCluePresenter

    val treasureChestId = "TreasureChestUuid"
    val clueText = "Some textClue text"

    @Before
    fun setUp() {
        clueConnection = Mockito.mock(ClueConnection::class.java)

        createClueView = Mockito.mock(CreateClueView::class.java)

        createCluePresenter = CreateCluePresenter(clueConnection)

        PresenterManager.addPresenter(CreateCluePresenter.TAG, createCluePresenter)

        assertTrue(PresenterManager.hasPresenter(CreateCluePresenter.TAG))
    }

    @After
    fun tearDown() {

    }

    @Test
    fun loadNewClue() {
        createCluePresenter.load(createClueView, treasureChestId)

        Mockito.verify(createClueView).setClueText("")
        Mockito.verify(clueConnection).getTextClueForParent(treasureChestId)
    }

    @Test
    fun loadExistingClue() {
        Mockito.`when`(clueConnection.getTextClueForParent(treasureChestId)).thenReturn(TextClue("uuid", treasureChestId, clueText))

        createCluePresenter.load(createClueView, treasureChestId)

        Mockito.verify(createClueView).setClueText(clueText)
        Mockito.verify(clueConnection).getTextClueForParent(treasureChestId)
    }

    @Test
    fun testSave() {
        createCluePresenter.load(createClueView, treasureChestId)

        createCluePresenter.save()

        assertFalse(PresenterManager.hasPresenter(CreateCluePresenter.TAG))

        Mockito.verify(createClueView).close()
        Mockito.verify(clueConnection).insert(MockitoMatchers.anyObject())
    }

    @Test
    fun testCancel() {
        createCluePresenter.load(createClueView, treasureChestId)

        createCluePresenter.cancel()

        assertFalse(PresenterManager.hasPresenter(CreateCluePresenter.TAG))

        Mockito.verify(createClueView).close()
    }
}