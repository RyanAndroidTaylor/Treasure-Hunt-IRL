package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.ui.container.CreateClueContainer
import java.util.*

/**
 * Created by ryantaylor on 6/28/16.
 */
class CreateCluePresenter : Presenter {

    companion object {
        val TAG = CreateCluePresenter::class.java.simpleName
    }

    private lateinit var createClueContainer: CreateClueContainer
    private lateinit var createHuntPresenter: CreateHuntPresenter

    private var clueText = ""

    fun load(createClueContainer: CreateClueContainer, createHuntPresenter: CreateHuntPresenter) {
        this.createClueContainer = createClueContainer
        this.createHuntPresenter = createHuntPresenter
    }

    fun onTextChanged(clueText: String) {
        this.clueText = clueText
    }

    fun save() {
        val clue = Clue(UUID.randomUUID().toString().replace("-", ""), createHuntPresenter.treasureHuntId, clueText)

        THApp.briteDatabase.insert(Clue.TABLE.NAME, clue.getContentValues())

        PresenterManager.removePresenter(TAG)

        createHuntPresenter.switchState(CreateHuntPresenter.CREATE_HUNT)
    }

    fun cancel() {
        PresenterManager.removePresenter(TAG)

        createHuntPresenter.switchState(CreateHuntPresenter.CREATE_HUNT)
    }
}