package ru.technostore.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Image(var image: String, var id: Int, val width: Int, val height: Int): Parcelable {
    val aspectRatio: Float get() = width.toFloat() / height.toFloat()
}