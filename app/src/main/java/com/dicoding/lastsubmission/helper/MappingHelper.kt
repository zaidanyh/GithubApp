package com.dicoding.lastsubmission.helper

import android.database.Cursor
import android.provider.BaseColumns._ID
import com.dicoding.lastsubmission.db.DatabaseContract.UserColumns.Companion.AVATAR
import com.dicoding.lastsubmission.db.DatabaseContract.UserColumns.Companion.FULLNAME
import com.dicoding.lastsubmission.db.DatabaseContract.UserColumns.Companion.STATUS
import com.dicoding.lastsubmission.db.DatabaseContract.UserColumns.Companion.USERNAME
import com.dicoding.lastsubmission.model.User

@Suppress("DEPRECATED_IDENTITY_EQUALS")
object MappingHelper {
    fun mappingCursor(cursor: Cursor?): ArrayList<User> {
        val listFav = ArrayList<User>()

        cursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(_ID))
                val username = getString(getColumnIndexOrThrow(USERNAME))
                val avatar = getString(getColumnIndexOrThrow(AVATAR))
                val fullname = getString(getColumnIndexOrThrow(FULLNAME))
                val status = getInt(getColumnIndexOrThrow(STATUS)) === 1
                listFav.add(User(id, username, avatar, fullname, status))
            }
        }
        return listFav
    }

    fun mappingCursorbyUsername(cursor: Cursor): User? {
        var user: User? = null

        if(cursor.moveToFirst()) {
            val id = cursor.getInt(0)
            val username = cursor.getString(1)
            val avatar = cursor.getString(2)
            val fullname = cursor.getString(3)
            val status = cursor.getInt(4) === 1

            user = User(id, username, avatar, fullname, status)
        }

        return user
    }
}