package com.dtprogramming.treasurehuntirl.database.connections

import com.dtprogramming.treasurehuntirl.database.models.PassPhrase

/**
 * Created by ryantaylor on 8/5/16.
 */
interface PassPhraseConnection : Connection {

    fun insert(passPhrase: PassPhrase)
    fun update(passPhrase: PassPhrase)
    fun delete(passPhrase: PassPhrase)
    fun delete(passPhraseUuid: String)
    fun deleteForParent(parentUuid: String)

    fun getPassPhraseForParent(passPhraseParentUuid: String): PassPhrase?
}