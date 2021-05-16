package com.mp.vocabulary.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class VocaDBHelper(
        val context: Context
) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){

    companion object {
        val DB_NAME = "vocabulary.db"
        val DB_VERSION = 1
        val TABLE_NOTE = "note"
        val TABLE_STAR = "star"
        val ENG = "eng"
        val KOR = "kor"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSql1 = "create able if not exists $TABLE_NOTE(" +
                "$ENG text," +
                "$KOR text);"
        val createTableSql2 = "create able if not exists $TABLE_STAR(" +
                "$ENG text," +
                "$KOR text);"
        db!!.execSQL(createTableSql1)
        db.execSQL(createTableSql2)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val dropTableSql1 = "drop table if exists $TABLE_NOTE;"
        val dropTableSql2 = "drop table if exists $TABLE_STAR;"
        db!!.execSQL(dropTableSql1)
        db.execSQL(dropTableSql2)
        onCreate(db)
    }
}