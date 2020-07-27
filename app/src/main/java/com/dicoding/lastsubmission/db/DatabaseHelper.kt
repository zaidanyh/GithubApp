package com.dicoding.lastsubmission.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID
import com.dicoding.lastsubmission.db.DatabaseContract.UserColumns
import com.dicoding.lastsubmission.db.DatabaseContract.UserColumns.Companion.TABLE

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE = "dbgithupapp"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE (${_ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${UserColumns.USERNAME} TEXT NOT NULL, " +
                "${UserColumns.AVATAR} TEXT NOT NULL, " +
                "${UserColumns.FULLNAME} TEXT NOT NULL, " +
                "${UserColumns.STATUS} BOOL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE")
        onCreate(db)
    }
}