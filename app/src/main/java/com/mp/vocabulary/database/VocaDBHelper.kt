package com.mp.vocabulary.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.mp.vocabulary.data.Voca

enum class DBTable {
    NOTE, STAR
}

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
        val createTableSql1 = "create table if not exists $TABLE_NOTE(" +
                "$ENG text," +
                "$KOR text);"
        val createTableSql2 = "create table if not exists $TABLE_STAR(" +
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

    fun getResultToVoca(cursor: Cursor): Voca{
        cursor.moveToFirst()

        val eng = cursor.getString(0)
        Log.d("this--->", eng)
        val meanings = mutableListOf<String>()

        do {
            meanings.add(cursor.getString(1))
        } while(cursor.moveToNext())

        return Voca(eng, meanings)
    }

    fun insertVoca(targetTable: DBTable, eng: String, kor: String): Boolean {
        val targetTableName: String = when(targetTable){
            DBTable.NOTE -> TABLE_NOTE
            DBTable.STAR -> TABLE_STAR
        }

        val values = ContentValues()
        values.put(ENG, eng)
        values.put(KOR, kor)

        val db = writableDatabase
        val flag = db.insert(targetTableName, null, values) > 0
        db.close()
        return flag
    }

    fun findVoca(targetTable: DBTable, eng: String): Voca? {
        val targetTableName: String = when(targetTable){
            DBTable.NOTE -> TABLE_NOTE
            DBTable.STAR -> TABLE_STAR
        }

        val selectSql = "select * from $targetTableName where $ENG='$eng';"
        val db = readableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0

        val result: Voca? = if(flag) getResultToVoca(cursor) else null
        cursor.close()
        db.close()

        return result
    }

    fun deleteVoca(targetTable: DBTable, eng: String): Boolean {
        val targetTableName: String = when(targetTable){
            DBTable.NOTE -> TABLE_NOTE
            DBTable.STAR -> TABLE_STAR
        }

        // 먼저 해당 pid 가 존재하는지 확인
        val selectSql = "select * from $targetTableName where $ENG='$eng';"
        val db = writableDatabase
        val cursor = db.rawQuery(selectSql, null)
        val flag = cursor.count != 0
        if(flag){
            cursor.moveToFirst()
            db.delete(targetTableName, "$ENG=?", arrayOf(eng))
        }
        cursor.close()
        db.close()
        return flag
    }

    fun findAll(targetTable: DBTable): MutableList<Voca>{
        val targetTableName: String = when(targetTable){
            DBTable.NOTE -> TABLE_NOTE
            DBTable.STAR -> TABLE_STAR
        }
        val selectAllSql = "select * from $targetTableName order by $ENG;"
        val db = readableDatabase
        val cursor = db.rawQuery(selectAllSql, null)
        val flag = cursor.count != 0
        val noteVocaList = mutableListOf<Voca>()

        cursor.moveToFirst()
        if(flag) {
            var word = cursor.getString(0)
            var meaningList = mutableListOf<String>()

            do {
                val eng = cursor.getString(0)
                val kor = cursor.getString(1)
                if(word != eng) {
                    noteVocaList.add(Voca(eng = word, kor = meaningList))
                    meaningList.clear()
                    word = eng
                }
                meaningList.add(kor)
            } while (cursor.moveToNext())
        }

        return noteVocaList
    }
}