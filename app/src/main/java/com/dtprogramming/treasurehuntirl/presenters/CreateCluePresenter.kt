package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.ui.views.CreateClueView
import java.util.*

/**
 * Created by ryantaylor on 6/28/16.
 */
class CreateCluePresenter : Presenter {

    companion object {
        val TAG: String = CreateCluePresenter::class.java.simpleName
    }

    private lateinit var createClueView: CreateClueView
    private lateinit var treasureHuntId: String

    private var clueText = ""

    fun load(createClueView: CreateClueView, treasureHuntId: String) {
        this.createClueView = createClueView
        this.treasureHuntId = treasureHuntId
    }

    fun onTextChanged(clueText: String) {
        this.clueText = clueText
    }

    fun save() {
        val clue = Clue(UUID.randomUUID().toString().replace("-", ""), treasureHuntId, clueText)

        THApp.briteDatabase.insert(Clue.TABLE.NAME, clue.getContentValues())

        PresenterManager.removePresenter(TAG)

        createClueView.finish()
    }

    fun cancel() {
        PresenterManager.removePresenter(TAG)

        createClueView.finish()
    }
}