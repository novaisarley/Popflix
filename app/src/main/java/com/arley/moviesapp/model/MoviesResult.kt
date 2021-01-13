package com.arley.moviesapp.model

import com.arley.moviesapp.model.Movie
import com.google.gson.annotations.SerializedName

data class MoviesResult(
    @SerializedName("page")
    var page: Int,

    @SerializedName("results")
    var results: MutableList<Movie>,

    @SerializedName("total_pages")
    var totalPages: Int,

    @SerializedName("total_results")
    var totalResults: Int
)