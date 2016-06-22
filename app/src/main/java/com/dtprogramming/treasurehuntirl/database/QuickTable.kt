package com.dtprogramming.treasurehuntirl.database

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy
import java.util.ArrayList

import android.support.annotation.StringDef

/**
 * Created by ryantaylor on 6/21/16.
 */
class QuickTable constructor() {

    private val SPACE = " "
    private val PERIOD = "."
    private val COMMA = ","

    private var createString = StringBuilder()

    private var foreignKeys = ArrayList<String>()

    private var currentTable: String? = null

    private var columns = ArrayList<String>()

    private var mClosed = true

    fun open(tableName: String): String {
        if (!mClosed)
            throw IllegalStateException("Table builder must be closed before opening a new one")

        prepareNewTable(tableName)

        createString.append("CREATE TABLE ")
        createString.append(currentTable)
        createString.append(" ( ")
        createString.append(TableColumns.ID)
        createString.append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
        createString.append(TableColumns.UUID)
        createString.append(SPACE)
        createString.append(DB_TEXT)
        createString.append(SPACE)
        createString.append(NOT_NULL)
        createString.append(SPACE)
        createString.append(UNIQUE)

        columns.add(currentTable + PERIOD + TableColumns.ID)
        columns.add(currentTable + PERIOD + TableColumns.UUID)

        return tableName
    }

    fun openWithUuidForeignKeyRestraint(tableName: String, referenceTable: String): String {
        if (!mClosed)
            throw IllegalStateException("Table builder must be closed before opening a new one")

        prepareNewTable(tableName)

        createString.append("CREATE TABLE ")
        createString.append(currentTable)
        createString.append(" ( ")
        createString.append(TableColumns.ID)
        createString.append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
        createString.append(TableColumns.UUID)
        createString.append(SPACE)
        createString.append(DB_TEXT)
        createString.append(SPACE)
        createString.append(NOT_NULL)
        createString.append(SPACE)
        createString.append(UNIQUE)
        createString.append(" REFERENCES ")
        createString.append(referenceTable)
        createString.append("(")
        createString.append(TableColumns.UUID)
        createString.append(")")

        columns.add(currentTable + PERIOD + TableColumns.ID)
        columns.add(currentTable + PERIOD + TableColumns.UUID)

        return tableName
    }

    private fun prepareNewTable(tableName: String) {
        mClosed = false
        createString = StringBuilder()
        columns = ArrayList<String>()
        foreignKeys = ArrayList<String>()
        currentTable = tableName
    }

    fun buildTextColumn(columnName: String): ColumnBuilder {
        return ColumnBuilder().appendText(columnName)
    }

    fun buildIntColumn(columnName: String): ColumnBuilder {
        return ColumnBuilder().appendInt(columnName)
    }

    fun buildBooleanColumn(columnName: String): ColumnBuilder {
        return ColumnBuilder().appendBoolean(columnName)
    }

    fun buildBlobColumn(columnName: String): ColumnBuilder {
        return ColumnBuilder().appendBlob(columnName)
    }

    fun end() {
        createString.append(" ")

        for (foreignKey in foreignKeys) {
            createString.append(COMMA)
            createString.append(SPACE)
            createString.append(foreignKey)
        }

        createString.append(")")
        mClosed = true
    }

    fun retrieveDropString(): String {
        if (!mClosed)
            end()

        return currentTable + " DROP TABLE IF EXISTS "
    }

    fun retrieveCreateString(): String {
        if (!mClosed)
            end()

        return createString.toString()
    }

    inner class ColumnBuilder {
        private lateinit var columnName: String

        private var type: String? = null

        private val constraints: MutableList<String>

        init {
            constraints = ArrayList<String>()
        }

        fun appendText(columnName: String): ColumnBuilder {
            this.columnName = columnName
            type = DB_TEXT

            return this
        }

        fun appendInt(columnName: String): ColumnBuilder {
            this.columnName = columnName

            type = DB_INTEGER

            return this
        }

        fun appendBoolean(columnName: String): ColumnBuilder {
            this.columnName = columnName

            type = DB_BOOLEAN

            return this
        }

        fun appendBlob(columnName: String): ColumnBuilder {
            this.columnName = columnName

            type = DB_BLOB

            return this
        }

        fun notNull(): ColumnBuilder {
            constraints.add(NOT_NULL)

            return this
        }

        fun foreignKey(tableName: String, columnName: String): ColumnBuilder {
            foreignKeys.add("FOREIGN KEY (" + this.columnName + ") " + REFERENCES + tableName + "(" + columnName + ")")

            return this
        }

        fun build(): String {
            createString.append(COMMA)
            createString.append(columnName)
            createString.append(SPACE)
            createString.append(type)

            for (constraint in constraints) {
                createString.append(SPACE)
                createString.append(constraint)
            }

            columns.add(currentTable + PERIOD + columnName)

            return columnName
        }
    }

    companion object {
        // Types
        val DB_INTEGER = "INTEGER"
        val DB_TEXT = "TEXT"
        val DB_BOOLEAN = "INTEGER"
        val DB_BLOB = "BLOB"

        //Constrains
        val NOT_NULL = "NOT NULL"
        val REFERENCES = "REFERENCES "
        val UNIQUE = "UNIQUE"

        private var mTableBuild: QuickTable? = null
    }
}
