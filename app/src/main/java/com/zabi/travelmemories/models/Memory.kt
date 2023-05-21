package com.zabi.travelmemories.models

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memory_table")
data class Memory(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String? = null,
    var image: String? = null,
    var date: String? = null,
    var place: String? = null,
    var location: Location? = null,
    var type: Int = 0,
    var mood: Double? = null,
    var notes: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable<Location>(Location::class.java.classLoader),
        parcel.readInt(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString()
    ) {
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(id)
        dest.writeString(name)
        dest.writeString(image)
        dest.writeString(date)
        dest.writeString(place)
        dest.writeParcelable(location, flags)
        dest.writeInt(type)
        dest.writeValue(mood)
        dest.writeString(notes)
    }

    companion object CREATOR : Parcelable.Creator<Memory> {
        override fun createFromParcel(parcel: Parcel): Memory {
            return Memory(parcel)
        }

        override fun newArray(size: Int): Array<Memory?> {
            return arrayOfNulls(size)
        }
    }
}
