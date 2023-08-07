package com.kursatmemis.burc_rehberi_app.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Horoscope(
    val detailLink: String,
    val imgLink: String,
    val name: String,
    val date: String
) : Parcelable

