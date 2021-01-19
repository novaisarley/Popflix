package com.arley.moviesapp.model

import com.google.gson.annotations.SerializedName

data class CastMember(
    @SerializedName("id")
    var id: Int,

    @SerializedName("known_for_department")
    var knownForDepartment: String,

    @SerializedName("original_name")
    var originalName: String,

    @SerializedName("popularity")
    var popularity: Double,

    @SerializedName("profile_path")
    var profilePath: String,

    @SerializedName("character")
    var character: String

){
    companion object{
        fun createEmptyCastMember(): CastMember{
            val crewMember : CastMember = CastMember(0, "", "", 0.0,
                "", "")

            return crewMember
        }
    }

    override fun equals(other: Any?): Boolean {
        val obj : CastMember = other as CastMember

        return this.originalName.equals(obj.originalName)
    }

    override fun hashCode(): Int {
        return this.originalName.toInt()
    }
}