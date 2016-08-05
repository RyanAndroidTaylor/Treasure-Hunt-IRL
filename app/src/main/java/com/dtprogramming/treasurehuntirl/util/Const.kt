package com.dtprogramming.treasurehuntirl.util

/**
 * Created by ryantaylor on 7/12/16.
 */
val INITIAL_TREASURE_CHEST = "-1"

val HUNT_UUID = "HuntUuid"
val PLAYING_HUNT_UUID = "PlayingHuntUuid"
val PARENT_UUID = "ParentUuid"
val TREASURE_CHEST_UUID = "TreasureChestUuid"
val CLUE_UUID = "ClueUuid"
val INITIAL_CHEST = "InitialChest"
val CHEST_ORDER = "TreasureChestOrder"
val NEW = "New"

// Inventory item types
val TEXT_CLUE = 0
val WAYPOINT = 1

// Treasure chest states
val OPEN = 0 // Only collected chests can be open
val CLOSED = 1 // Can change to open. Only collected chests can be opened
val LOCKED = 2 // Can change to open. Only collected chests can be opened
val BURIED = 3 // Can change to closed
val BURIED_LOCKED = 4 // Can change to locked