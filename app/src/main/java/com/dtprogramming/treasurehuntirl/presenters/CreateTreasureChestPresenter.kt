package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.ui.views.CreateTreasureChestView
import com.dtprogramming.treasurehuntirl.util.randomUuid

/**
 * Created by ryantaylor on 7/11/16.
 */
class CreateTreasureChestPresenter : Presenter {

    companion object {
        val TAG: String = CreateTreasureChestPresenter::class.java.simpleName
    }

    private lateinit var createTreasureChestView: CreateTreasureChestView

    lateinit var treasureChestId: String
        private set

    private var new = false

    fun newTreasureChest(createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView

        treasureChestId = randomUuid()
        new = true
    }

    fun loadTreasureChest(treasureChestId: String, createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView
        this.treasureChestId = treasureChestId
    }

    fun reload(createTreasureChestView: CreateTreasureChestView) {
        this.createTreasureChestView = createTreasureChestView
    }

    fun titleChanged(newTitle: String) {

    }

    fun mapLoaded() {

    }

    fun cancel() {

        PresenterManager.removePresenter(TAG)
    }

    fun save() {
        PresenterManager.removePresenter(TAG)
    }
}