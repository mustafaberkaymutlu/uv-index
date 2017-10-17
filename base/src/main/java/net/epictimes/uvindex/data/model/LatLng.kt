package net.epictimes.uvindex.data.model

import android.os.Parcel
import android.os.Parcelable

data class LatLng(private val latitude: Double, private val longitude: Double) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readDouble(), parcel.readDouble())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<LatLng> {
        override fun createFromParcel(parcel: Parcel): LatLng = LatLng(parcel)

        override fun newArray(size: Int): Array<LatLng?> = arrayOfNulls(size)
    }

}