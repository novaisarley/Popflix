package com.arley.moviesapp.model

import com.google.gson.annotations.SerializedName

data class CreditsResult(
    @SerializedName("id")
    var id: Int,

    @SerializedName("cast")
    var cast: MutableList<CastMember>,

    @SerializedName("crew")
    var crew: MutableList<CrewMember>
)