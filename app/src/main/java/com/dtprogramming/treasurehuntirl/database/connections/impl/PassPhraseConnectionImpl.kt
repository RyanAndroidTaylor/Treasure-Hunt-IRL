package com.dtprogramming.treasurehuntirl.database.connections.impl

import android.database.sqlite.SQLiteDatabase
import com.dtprogramming.treasurehuntirl.THApp
import com.dtprogramming.treasurehuntirl.database.TableColumns
import com.dtprogramming.treasurehuntirl.database.connections.PassPhraseConnection
import com.dtprogramming.treasurehuntirl.database.models.PassPhrase
import rx.Subscription
import java.util.*

/**
 * Created by ryantaylor on 8/5/16.
 */
class PassPhraseConnectionImpl : PassPhraseConnection {

    override val database = THApp.briteDatabase

    override val subscriptions = ArrayList<Subscription>()

    override fun insert(passPhrase: PassPhrase) {
        database.insert(PassPhrase.TABLE.NAME, passPhrase.getContentValues(), SQLiteDatabase.CONFLICT_REPLACE)
    }

    override fun update(passPhrase: PassPhrase) {
        database.update(PassPhrase.TABLE.NAME, passPhrase.getContentValues(), TableColumns.WHERE_UUID_EQUALS, passPhrase.uuid)
    }

    override fun delete(passPhrase: PassPhrase) {
        delete(passPhrase.uuid)
    }

    override fun delete(passPhraseUuid: String) {
        database.delete(PassPhrase.TABLE.NAME, TableColumns.WHERE_UUID_EQUALS, passPhraseUuid)
    }

    override fun deleteForParent(parentUuid: String) {
        database.delete(PassPhrase.TABLE.NAME, "${PassPhrase.TABLE.PARENT}=?", parentUuid)
    }

    override fun getPassPhraseForParent(passPhraseParentUuid: String): PassPhrase? {
        var passPhrase: PassPhrase? = null
        val cursor = database.query("SELECT * FROM ${PassPhrase.TABLE.NAME} WHERE ${PassPhrase.TABLE.PARENT}=?", passPhraseParentUuid)

        if (cursor != null && cursor.moveToFirst()) {
            passPhrase = PassPhrase(cursor)

            cursor.close()
        }

        return passPhrase
    }

    override fun unsubscribe() {
        for (subscription in subscriptions)
            subscription.unsubscribe()
    }
}