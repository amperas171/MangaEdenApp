package com.amperas17.mangaedenapp.model.page

import android.os.Parcel
import android.os.Parcelable


data class Page(var number: Int, var url: String?, var weight: Int, var height: Int) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(number)
        parcel.writeString(url)
        parcel.writeInt(weight)
        parcel.writeInt(height)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Page> {
        override fun createFromParcel(parcel: Parcel): Page {
            return Page(parcel)
        }

        override fun newArray(size: Int): Array<Page?> {
            return arrayOfNulls(size)
        }
    }
}
