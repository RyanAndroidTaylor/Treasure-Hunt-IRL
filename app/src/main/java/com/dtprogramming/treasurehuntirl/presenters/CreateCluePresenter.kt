package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.ClueConnection
import com.dtprogramming.treasurehuntirl.database.models.TextClue
import com.dtprogramming.treasurehuntirl.ui.views.CreateClueView
import com.dtprogramming.treasurehuntirl.util.randomUuid
import javax.inject.Inject

/**
 * Created by ryantaylor on 6/28/16.
 */
class CreateCluePresenter() : Presenter {

    @Inject
    lateinit var clueConnection: ClueConnection

    companion object {
        val TAG: String = CreateCluePresenter::class.java.simpleName
    }

    private var createClueView: CreateClueView? = null

    private lateinit var clueUuid: String
    private lateinit var parentUuid: String

    private var clueText = ""

    init {
        THApp.databaseComponent.inject(this)
    }

    fun create(createClueView: CreateClueView, parentUuid: String) {
        this.createClueView = createClueView
        this.parentUuid = parentUuid

        clueUuid = randomUuid()

        clueConnection.insert(TextClue(clueUuid, parentUuid, ""))
    }

    fun load(createClueView: CreateClueView, clueUuid: String, parentUuid: String) {
        this.createClueView = createClueView
        this.clueUuid = clueUuid
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

    override fun dispose() {
        unsubscribe()

        PresenterManager.removePresenter(TAG)
    }

    private fun loadClue(clueUuid: String) {
        val clue = clueConnection.getTextClue(clueUuid)

        clueText = clue.text

        createClueView?.setClueText(clueText)
    }

    fun onTextChanged(clueText: String) {
        this.clueText = clueText
    }

    fun save() {
        clueConnection.update(TextClue(clueUuid, parentUuid, clueText))

        PresenterManager.removePresenter(TAG)

        createClueView?.close()
    }

    fun cancel() {
        PresenterManager.removePresenter(TAG)

        createClueView?.close()
    }
}