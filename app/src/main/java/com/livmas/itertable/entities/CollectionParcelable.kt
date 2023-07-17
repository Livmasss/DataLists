package com.livmas.itertable.entities

import android.os.Parcel
import android.os.Parcelable

class CollectionParcelable(
    var id: Int? = null,
    var name: String = "",
    var typeId: Int = 0
) : Parcelable {

    constructor(parcel: Parcel) : this() {
        this.id = parcel.readInt()
        this.typeId = parcel.readInt()
        this.name = parcel.readString().orEmpty()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        if (id == null) {
            id = 0
        }
        out.writeInt(id!!)
        out.writeInt(typeId)
        out.writeString(name)
    }

    companion object CREATOR : Parcelable.Creator<CollectionParcelable> {
        override fun createFromParcel(parcel: Parcel): CollectionParcelable {
            return CollectionParcelable(parcel)
        }

        override fun newArray(size: Int): Array<CollectionParcelable?> {
            return arrayOfNulls(size)
        }
    }
}