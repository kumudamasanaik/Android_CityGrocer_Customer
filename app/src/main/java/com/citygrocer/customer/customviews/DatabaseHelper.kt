package com.citygrocer.customer.customviews

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.citygrocer.customer.module.input.Details

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val CREATE_TABLE = ("CREATE TABLE "
            + TABLE_HISTORY + "("
            + LOG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + LOG_NAME + " TEXT"
            + ")")
    private val DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_HISTORY
    val details: List<Details>
        get() {
            val column = arrayOf<String>(LOG_ID, LOG_NAME)
            val sortorder = LOG_NAME + " ASC"
            val list = ArrayList<Details>()
            val db = this.readableDatabase
            val cursor = db.query(TABLE_HISTORY, column, null, null, null, null, sortorder)
            if (cursor.moveToFirst()) {
                do {
                    val details = Details(id = cursor.getString(cursor.getColumnIndex(LOG_ID)),
                            name = cursor.getString(cursor.getColumnIndex(LOG_NAME)))
                    list.add(details)
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return list
        }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, i: Int, i1: Int) {
        sqLiteDatabase.execSQL(DROP_TABLE)
    }

    fun addDetails(details: Details) {
        val sqLiteDatabase = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(LOG_NAME, details.name)
        sqLiteDatabase.insert(TABLE_HISTORY, null, contentValues)
        sqLiteDatabase.close()
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "history.db"
        private val TABLE_HISTORY = "log"
        private val LOG_ID = "log_id"
        private val LOG_NAME = "log_name"
       // private val LOG_ADDRESS = "log_address"
    }
}