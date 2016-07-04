package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.ui.views.CreateClueView

/**
 * Created by ryantaylor on 6/28/16.
 */
class CreateCluePresenter : Presenter {

    companion object {
        val TAG = CreateCluePresenter::class.java.simpleName
    }

    private lateinit var createClueView: CreateClueView

    private var clueText = ""

    fun load(createClueView: CreateClueView) {
        this.createClueView = createClueView
    }

    fun onTextChanged(clueText: String) {
        this.clueText = clueText
    }

    fun save() {
//        val clue = Clue(UUID.randomUUID().toString().replace("-", ""), createHuntPresenter.treasureHuntId, clueText)

//        createHuntPresenter.saveClue(clue)

        PresenterManager.removePresenter(TAG)

        createClueView.back()

//        createHuntPresenter.switchState(CreateHuntPresenter.CREATE_HUNT)
    }

    fun cancel() {
        PresenterManager.removePresenter(TAG)

        createClueView.back()

//        createHuntPresenter.switchState(CreateHuntPresenter.CREATE_HUNT)
    }
}