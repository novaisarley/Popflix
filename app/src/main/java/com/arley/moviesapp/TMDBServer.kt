package com.arley.moviesapp

import com.arley.moviesapp.model.CreditsResult
import com.arley.moviesapp.model.MoviesResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface TMDBServer {

    //Returns the top rated movies
    @GET("movie/top_rated?api_key=${Constants.API_KEY}")
    fun getTopRatedMovies() : Call<MoviesResult>

    //Returns the current popular movies
    @GET("movie/popular?api_key=${Constants.API_KEY}")
    fun getPopularMovies() : Call<MoviesResult>

    @GET("tv/popular?api_key=${Constants.API_KEY}")
    fun getPopularShows() : Call<MoviesResult>

    //Returns the cast and the crew of a especific movie by movie_id
    @GET("movie/{movie_id}/credits?api_key=${Constants.API_KEY}")
    fun getCredits(@Path("movie_id") movieId : Int) : Call<CreditsResult>
}