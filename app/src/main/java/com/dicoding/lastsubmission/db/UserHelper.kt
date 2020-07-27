package com.dicoding.lastsubmission.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.provider.BaseColumns._ID
import com.dicoding.lastsubmission.db.DatabaseContract.UserColumns.Companion.TABLE
import com.dicoding.lastsubmission.db.DatabaseContract.UserColumns.Companion.USERNAME
import java.sql.SQLException

class UserHelper(context: Context) {
    private var databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private const val DATABASE_TABLE = TABLE
        private var INSTANCE: UserHelper? = null

        fun instance(context: Context): UserHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: UserHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = databaseHelper.writableDatabase
    }

    fun close() {
        databaseHelper.close()

        if (database.isOpen) {
            database.close()
        }
    }

    fun getAll(): Cursor {
        return database.query(DATABASE_TABLE, null, null, null, null, null, "$_ID ASC")
    }

    fun getByUsername(username: String): Cursor {
        return database.query(DATABASE_TABLE, null, "$USERNAME = ?", arrayOf(username), null, null, null, null)
    }

    fun insert(value: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, value)
    }

    fun delete(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
}