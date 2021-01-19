package com.arley.moviesapp.model

import com.google.gson.annotations.SerializedName

data class CrewMember(
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

    @SerializedName("job")
    var job: String

){
    companion object{
        fun createEmptyCrewMember(): CrewMember{
            val crewMember : CrewMember = CrewMember(0, "", "", 0.0,
            "", "")

            return crewMember
        }
    }

    override fun equals(other: Any?): Boolean {
        val obj : CrewMember = other as CrewMember

        return this.originalName.equals(obj.originalName)
    }

}