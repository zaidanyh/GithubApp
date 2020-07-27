package com.dicoding.lastsubmission.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User (
    var id: Int = 0,
    var login: String,
    var avatar_url: String,
    var name: String,
    var status: Boolean = false
):Parcelable