package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.ui.views.CreateClueView
import com.dtprogramming.treasurehuntirl.util.randomUuid

/**
 * Created by ryantaylor on 6/28/16.
 */
class CreateCluePresenter(val clueConnection: ClueConnection) : Presenter {

    companion object {
        val TAG: String = CreateCluePresenter::class.java.simpleName
    }

    private var createClueView: CreateClueView? = null

    private lateinit var clueUuid: String
    private lateinit var parentUuid: String

    private var new = false
    private var clueText = ""

    fun load(createClueView: CreateClueView, parentUuid: String) {
        this.createClueView = createClueView
        this.parentUuid = parentUuid

        loadClue(parentUuid)

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

    private fun loadClue(parentUuid: String) {
        val clue = clueConnection.getClueForParent(parentUuid)

        if (clue != null) {
            clueUuid = clue.uuid
            clueText = clue.text
        } else {
            new = true
            clueUuid = randomUuid()
        }
    }

    fun onTextChanged(clueText: String) {
        this.clueText = clueText
    }

    fun save() {
        if (new)
            clueConnection.insert(Clue(clueUuid, parentUuid, clueText))
        else
            clueConnection.update(Clue(clueUuid, parentUuid, clueText))

        PresenterManager.removePresenter(TAG)

        createClueView?.close()
    }

    fun cancel() {
        PresenterManager.removePresenter(TAG)

        createClueView?.close()
    }
}