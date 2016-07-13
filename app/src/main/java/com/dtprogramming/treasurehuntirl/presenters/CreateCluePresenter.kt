package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.ui.views.CreateClueView
import java.util.*

/**
 * Created by ryantaylor on 6/28/16.
 */
class CreateCluePresenter(val clueConnection: ClueConnection) : Presenter {

    companion object {
        val TAG: String = CreateCluePresenter::class.java.simpleName
    }

    private lateinit var createClueView: CreateClueView

    private lateinit var clueId: String
    private lateinit var treasureHuntId: String

    private var clueText = ""

    fun create(createClueView: CreateClueView, treasureHuntId: String) {
        this.createClueView = createClueView
        this.treasureHuntId = treasureHuntId

        clueConnection.insert(Clue(clueId, treasureHuntId, clueText))
    }

    fun load(createClueView: CreateClueView, treasureChestId: String) {
        this.createClueView = createClueView
        this.treasureHuntId = treasureChestId

        val clue = clueConnection.getClueForTreasureChest(treasureChestId)

        clue?.let {
            clueId = it.uuid
            clueText = it.text
        }

        createClueView.setClueText(clueText)
    }

    fun reload(createClueView: CreateClueView) {
        this.createClueView = createClueView

        createClueView.setClueText(clueText)
    }

    fun onTextChanged(clueText: String) {
        this.clueText = clueText
    }

    fun save() {
        clueConnection.update(Clue(clueId, treasureHuntId, clueText))

        PresenterManager.removePresenter(TAG)

        createClueView.close()
    }

    fun cancel() {
        PresenterManager.removePresenter(TAG)

        createClueView.close()
    }
}