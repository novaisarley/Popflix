package com.arley.moviesapp.model

import com.google.gson.annotations.SerializedName

data class CreditsResult(
    @SerializedName("id")
    var id: Int,

    @SerializedName("cast")
    var cast: List<CastMember>,

    @SerializedName("crew")
    var crew: List<CrewMember>
)