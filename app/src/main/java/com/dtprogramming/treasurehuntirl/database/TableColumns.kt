package com.dtprogramming.treasurehuntirl.database

/**
 * Created by ryantaylor on 6/21/16.
 */
interface TableColumns {

    companion object {
        val ID = "_id"
        val UUID = "UUID"

        val NULL_ID: Long = -1

        val WHERE_ID_EQUALS = ID + "=?"
        val WHERE_UUID_EQUALS = UUID + "=?"
    }
}
