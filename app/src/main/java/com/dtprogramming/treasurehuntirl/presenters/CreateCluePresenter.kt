package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.ui.views.CreateClueView
import com.dtprogramming.treasurehuntirl.util.randomUuid
import java.util.*

/**
 * Created by ryantaylor on 6/28/16.
 */
class CreateCluePresenter(val clueConnection: ClueConnection) : Presenter {

    companion object {
        val TAG: String = CreateCluePresenter::class.java.simpleName
    }

    private var createClueView: CreateClueView? = null

    private lateinit var clueId: String
    private lateinit var treasureHuntId: String

    private var new = false
    private var clueText = ""

    fun load(createClueView: CreateClueView, treasureChestId: String) {
        this.createClueView = createClueView
        this.treasureHuntId = treasureChestId

        loadClue(treasureChestId)

        createClueView.setClueText(clueText)
    }

    fun reload(createClueView: CreateClueView) {
        this.createClueView = createClueView

        createClueView.setClueText(clueText)
    }

    override fun unsubscribe() {
        clueConnection.unsubscribe()

        createClueView = null
    }

    override fun finish() {
        PresenterManager.removePresenter(TAG)
    }

    private fun loadClue(treasureChestId: String) {
        val clue = clueConnection.getClueForTreasureChest(treasureChestId)

        if (clue != null) {
            clueId = clue.uuid
            clueText = clue.text
        } else {
            new = true
            clueId = randomUuid()
        }
    }

    fun onTextChanged(clueText: String) {
        this.clueText = clueText
    }

    fun save() {
        if (new)
            clueConnection.insert(Clue(clueId, treasureHuntId, clueText))
        else
            clueConnection.update(Clue(clueId, treasureHuntId, clueText))

        PresenterManager.removePresenter(TAG)

        createClueView?.close()
    }

    fun cancel() {
        PresenterManager.removePresenter(TAG)

        createClueView?.close()
    }
}