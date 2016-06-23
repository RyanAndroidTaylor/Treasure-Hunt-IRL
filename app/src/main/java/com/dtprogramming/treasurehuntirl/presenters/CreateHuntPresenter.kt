package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.DatabaseObservables
import com.dtprogramming.treasurehuntirl.database.models.Clue
import com.dtprogramming.treasurehuntirl.ui.views.CreateHuntView
import rx.Subscription
import java.util.*

/**
 * Created by ryantaylor on 6/20/16.
 */
class CreateHuntPresenter() : Presenter {

    private var state = CREATE_HUNT

    private lateinit var treasureHuntId: String
    private lateinit var createHuntView: CreateHuntView

    private var clueSubscription: Subscription? = null

    var clues = ArrayList<String>()
        private set

    companion object {
        val TAG = CreateHuntPresenter::class.java.simpleName

        val CREATE_HUNT = 0
        val CREATE_CLUE = 1
        val CREATE_WAY_POINT = 2
    }

    fun load(treasureHuntId: String, createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView
        this.treasureHuntId = treasureHuntId

        createHuntView.initLoad(clues)

        subscribeToClues()
    }

    fun reload(createHuntView: CreateHuntView) {
        this.createHuntView = createHuntView

        loadContainer()

        subscribeToClues()
    }

    fun saveAndFinish() {
        //TODO Save treasure hunt once it is setup

        finish()
    }

    fun finish() {
        unsubscribeToClues()

        PresenterManager.removePresenter(TAG)
    }

    private fun loadContainer() {
        when (state) {
            CREATE_HUNT -> {
                subscribeToClues()

                createHuntView.loadCreateHuntContainer(clues)
            }
            CREATE_CLUE -> {
                unsubscribeToClues()

                createHuntView.loadCreateClueContainer()
            }
            CREATE_WAY_POINT -> {
                unsubscribeToClues()

                createHuntView.loadCreateWayPointContainer()
            }
        }
    }

    private fun subscribeToClues() {
        clueSubscription = DatabaseObservables.getClueObservable(treasureHuntId)
                .subscribe ({
                    val clueList = ArrayList<String>()

                    for (clue in it) {
                        clueList.add(clue.text)
                    }

                    clues = clueList

                    createHuntView.updateClueList(clues)
                })
    }

    private fun unsubscribeToClues() {
        clueSubscription?.let {
            if (!it.isUnsubscribed)
                it.unsubscribe()
        }
    }

    fun switchState(newState: Int) {
        state = newState

        loadContainer()
    }

    fun saveClue(clueText: String) {
        val clue = Clue(UUID.randomUUID().toString().replace("-", ""), treasureHuntId, clueText)

        THApp.briteDatabase.insert(Clue.TABLE.NAME, clue.getContentValues())
    }
}