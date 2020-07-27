package com.dicoding.lastsubmission.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Items(
    var login: String,
    var avatar_url: String
): Parcelable