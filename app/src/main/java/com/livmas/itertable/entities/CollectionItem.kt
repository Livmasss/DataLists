package com.livmas.itertable.entities

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collections")
data class CollectionItem(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "type_id")
    var type: CollectionType,
    @ColumnInfo(name = "number", defaultValue = "0")
    var number: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString().orEmpty(),
        CollectionType.valueOf(parcel.readInt())!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeInt(type.id)
        parcel.writeInt(number)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CollectionItem> {
        override fun createFromParcel(parcel: Parcel): CollectionItem {
            return CollectionItem(parcel)
        }

        override fun newArray(size: Int): Array<CollectionItem?> {
            return arrayOfNulls(size)
        }
    }
}