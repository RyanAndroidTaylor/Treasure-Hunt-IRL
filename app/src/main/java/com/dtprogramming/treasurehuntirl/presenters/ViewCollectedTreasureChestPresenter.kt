package com.dtprogramming.treasurehuntirl.presenters

import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.connections.CollectedTreasureChestConnection
import com.dtprogramming.treasurehuntirl.database.connections.InventoryConnection
import com.dtprogramming.treasurehuntirl.database.connections.PassPhraseConnection
import com.dtprogramming.treasurehuntirl.database.models.CollectedTreasureChest
import com.dtprogramming.treasurehuntirl.database.models.PassPhrase
import com.dtprogramming.treasurehuntirl.ui.views.ViewCollectedTreasureChestView
import com.dtprogramming.treasurehuntirl.util.LOCKED
import com.dtprogramming.treasurehuntirl.util.OPEN
import javax.inject.Inject

/**
 * Created by ryantaylor on 7/26/16.
 */
class ViewCollectedTreasureChestPresenter() : Presenter {

    companion object {
        val TAG: String = ViewCollectedTreasureChestPresenter::class.java.simpleName
    }

    @Inject
    lateinit var collectedTreasureChestConnection: CollectedTreasureChestConnection
    @Inject
    lateinit var inventoryConnection: InventoryConnection
    @Inject
    lateinit var passPhraseConnection: PassPhraseConnection

    private var viewCollectedTreasureChestView: ViewCollectedTreasureChestView? = null

    lateinit var collectedTreasureChestUuid: String
        private set

    private lateinit var collectedTreasureChest: CollectedTreasureChest

    private var passPhrase: PassPhrase? = null
    private var passPhraseGuess = ""

    init {
        THApp.databaseComponent.inject(this)
    }

    fun load(viewCollectedTreasureChestView: ViewCollectedTreasureChestView, collectedTreasureChestUuid: String) {
        this.viewCollectedTreasureChestView = viewCollectedTreasureChestView
        this.collectedTreasureChestUuid = collectedTreasureChestUuid

        collectedTreasureChest = collectedTreasureChestConnection.getCollectedTreasureChest(collectedTreasureChestUuid)

        updateForTreasureChestState()
    }

    fun reload(viewCollectedTreasureChestView: ViewCollectedTreasureChestView) {
        this.viewCollectedTreasureChestView = viewCollectedTreasureChestView

        updateForTreasureChestState()
    }

    override fun unsubscribe() {
        viewCollectedTreasureChestView = null

        collectedTreasureChestConnection.unsubscribe()
    }
    override fun dispose() {

        unsubscribe()

        PresenterManager.removePresenter(TAG)
    }

    fun updatePassPhrase(newPassPhraseGuess: String) {
        passPhraseGuess = newPassPhraseGuess
    }

    fun performTreasureChestAction() {
        if (isTreasureChestLocked()) {
            if (correctPassPhraseGuess()) {
                openTreasureChest()
            } else {
                viewCollectedTreasureChestView?.displayIncorrectPassPhraseGuess()
            }
        } else if (isTreasureChestOpen()) {
            viewCollectedTreasureChestView?.close()
        } else {
            openTreasureChest()
        }
    }

    private fun correctPassPhraseGuess(): Boolean {
        return passPhraseGuess.equals(passPhrase?.text)
    }

    private fun updateForTreasureChestState() {
        if (isTreasureChestLocked()) {
            passPhrase = passPhraseConnection.getPassPhraseForParent(collectedTreasureChestUuid)

            viewCollectedTreasureChestView?.displayLockedTreasureChest()
        } else if (isTreasureChestOpen()) {
            viewCollectedTreasureChestView?.displayOpenedTreasureChest()
            getAndDisplayCollectedItems()
        } else {
            viewCollectedTreasureChestView?.displayClosedTreasureChest()
        }
    }

    private fun openTreasureChest() {
        viewCollectedTreasureChestView?.displayOpenedTreasureChest()

        collectedTreasureChest = collectedTreasureChestConnection.openCollectedTreasureChest(collectedTreasureChest, { getAndDisplayCollectedItems() })
    }

    private fun getAndDisplayCollectedItems() {
        inventoryConnection.getCollectedItemsForTreasureChestAsync(collectedTreasureChestUuid, { viewCollectedTreasureChestView?.displayCollectedItems(it) })
    }

    private fun isTreasureChestOpen(): Boolean {
        return collectedTreasureChest.state == OPEN
    }

    private fun isTreasureChestLocked(): Boolean {
        return collectedTreasureChest.state == LOCKED
    }
}