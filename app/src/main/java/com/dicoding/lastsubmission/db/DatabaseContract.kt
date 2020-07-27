package com.dicoding.lastsubmission.db

import android.provider.BaseColumns

class DatabaseContract {

    internal class UserColumns: BaseColumns {

        companion object {
            const val TABLE = "user"
            const val USERNAME = "username"
            const val AVATAR = "avatar"
            const val FULLNAME = "fullname"
            const val STATUS = "status"
        }
    }
}