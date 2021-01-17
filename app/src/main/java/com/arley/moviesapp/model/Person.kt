package com.arley.moviesapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Person(
    @SerializedName("known_for_department")
    var knownForDepartment: String?,

    @SerializedName("profile_path")
    var profilePath: String?,

    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String?,

    @SerializedName("adult")
    var adult: Boolean,

    @SerializedName("popularity")
    var popularity: Double

    ) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(knownForDepartment)
        parcel.writeString(profilePath)
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeByte(if (adult) 1 else 0)
        parcel.writeDouble(popularity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Person> {
        override fun createFromParcel(parcel: Parcel): Person {
            return Person(parcel)
        }

        fun createEmptyPerson(): Person{
            val person : Person = Person(name = "", popularity = 0.0, id = 0, adult = false, knownForDepartment = "", profilePath = "")
            return person
        }

        override fun newArray(size: Int): Array<Person?> {
            return arrayOfNulls(size)
        }
    }
}