package com.arley.moviesapp.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TvShow(
    @SerializedName("backdrop_path")
    var backdropPath: String?,

    @SerializedName("first_air_date")
    var firstAirDate: String?,

    @SerializedName("id")
    var id: Int,

    @SerializedName("name")
    var name: String?,

    @SerializedName("original_language")
    var originalLanguage: String?,

    @SerializedName("original_name")
    var originalname: String?,

    @SerializedName("overview")
    var overview: String?,

    @SerializedName("popularity")
    var popularity: Double,

    @SerializedName("poster_path")
    var posterPath: String?,

    @SerializedName("vote_average")
    var voteAverage: Float,

    @SerializedName("vote_count")
    var voteCount: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readFloat(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(backdropPath)
        parcel.writeString(firstAirDate)
        parcel.writeInt(id)
        parcel.writeString(name)
        parcel.writeString(originalLanguage)
        parcel.writeString(originalname)
        parcel.writeString(overview)
        parcel.writeDouble(popularity)
        parcel.writeString(posterPath)
        parcel.writeFloat(voteAverage)
        parcel.writeInt(voteCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TvShow> {
        override fun createFromParcel(parcel: Parcel): TvShow {
            return TvShow(parcel)
        }

        fun createEmptyTvShow(): TvShow {
            val tvShow: TvShow = TvShow(backdropPath = "", id = 0, posterPath = "", name = "Loading...",
                firstAirDate = "", voteCount = 0, voteAverage = 0.0f, popularity = 0.0, overview = "",
                originalname = "Loading...", originalLanguage = "")
            return tvShow
        }

        override fun newArray(size: Int): Array<TvShow?> {
            return arrayOfNulls(size)
        }
    }

}