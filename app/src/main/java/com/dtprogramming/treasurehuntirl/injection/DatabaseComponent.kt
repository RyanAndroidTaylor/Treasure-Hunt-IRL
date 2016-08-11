package com.dtprogramming.treasurehuntirl.injection

import com.dtprogramming.treasurehuntirl.presenters.*
import com.dtprogramming.treasurehuntirl.ui.container.CreateTreasureHuntListContainer
import com.dtprogramming.treasurehuntirl.ui.container.PlayTreasureHuntListContainer
import com.dtprogramming.treasurehuntirl.ui.container.SearchTreasureHuntContainer
import com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter.InventoryAdapter
import com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter.PlayingTreasureHuntAdapter
import com.dtprogramming.treasurehuntirl.ui.recycler_view.adapter.TreasureHuntAdapter
import dagger.Component
import javax.inject.Singleton

/**
 * Created by ryantaylor on 8/7/16.
 */
@Singleton
@Component(modules = arrayOf(DatabaseModule::class))
interface DatabaseComponent {

    fun inject(createCluePresenter: CreateCluePresenter)
    fun inject(createHuntPresenter: CreateHuntPresenter)
    fun inject(createTreasureChestPresenter: CreateTreasureChestPresenter)
    fun inject(createWaypointPresenter: CreateWaypointPresenter)
    fun inject(digModePresenter: DigModePresenter)
    fun inject(playTreasureHuntPresenter: PlayTreasureHuntPresenter)
    fun inject(viewCollectedTreasureChestPresenter: ViewCollectedTreasureChestPresenter)
    fun inject(viewTreasureHuntPresenter: ViewTreasureHuntPresenter)

    //TODO Should find a better way to handle these cases
    fun inject(inventoryViewHolder: InventoryAdapter.InventoryViewHolder)
    fun inject(playingTreasureHuntViewHolder: PlayingTreasureHuntAdapter.PlayingTreasureHuntViewHolder)
    fun inject(treasureHuntViewHolder: TreasureHuntAdapter.TreasureHuntViewHolder)
    fun inject(createTreasureHuntListContainer: CreateTreasureHuntListContainer)
    fun inject(searchTreasureHuntContainer: SearchTreasureHuntContainer)
    fun inject(playTreasureHuntListContainer: PlayTreasureHuntListContainer)
}