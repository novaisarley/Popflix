package com.arley.moviesapp.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("adult")
    var adult: Boolean,

    @SerializedName("backdrop_path")
    var backdropPath: String?,

    @SerializedName("id")
    var id: Int,

    @SerializedName("original_language")
    var originalLanguage: String,

    @SerializedName("original_title")
    var originalTitle: String,

    @SerializedName("overview")
    var overview: String,

    @SerializedName("popularity")
    var popularity: Double,

    @SerializedName("poster_path")
    var posterPath: String,

    @SerializedName("release_date")
    var releaseDate: String,

    @SerializedName("title")
    var title: String,

    @SerializedName("vote_average")
    var voteAverage: Float,

    @SerializedName("adult")
    var voteCount: Int
)