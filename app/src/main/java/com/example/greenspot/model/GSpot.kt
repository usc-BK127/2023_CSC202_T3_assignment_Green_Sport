package com.example.greenspot.model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "g_spot_table")
data class GSpot (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var title: String = "",
    var place: String = "",
    val date: String = "",
    val location: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val image: String = "",
    val share: String = "",
    ): Parcelable