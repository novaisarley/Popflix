package com.arley.moviesapp

import com.arley.moviesapp.model.CreditsResult
import com.arley.moviesapp.model.MoviesResult
import com.arley.moviesapp.model.PeopleResult
import com.arley.moviesapp.model.TvShowResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBServer {

    //Returns the top rated movies
    @GET("movie/top_rated?api_key=${Constants.API_KEY}")
    fun getTopRatedMovies() : Call<MoviesResult>

    //Returns the current popular movies
    @GET("movie/popular?api_key=${Constants.API_KEY}")
    fun getPopularMovies() : Call<MoviesResult>

    @GET("tv/popular?api_key=${Constants.API_KEY}")
    fun getPopularShows() : Call<TvShowResult>

    @GET("trending/person/day?api_key=${Constants.API_KEY}")
    fun getTrendingPeople() : Call<PeopleResult>

    //Returns the cast and the crew of a especific movie by movie_id
    @GET("movie/{movie_id}/credits?api_key=${Constants.API_KEY}")
    fun getMovieCredits(@Path("movie_id") movieId : Int) : Call<CreditsResult>

    //Returns the cast and the crew of a especific movie by show_id
    @GET("tv/{show_id}/credits?api_key=${Constants.API_KEY}")
    fun getTvShowCredits(@Path("show_id") showId : Int) : Call<CreditsResult>

    @GET("search/movie?api_key=${Constants.API_KEY}&page=1")
    fun getMovieSearchByTiltle(@Query("query") query : String) : Call<MoviesResult>

    @GET("search/tv?api_key=${Constants.API_KEY}&page=1")
    fun getTvShowsSearchByTiltle(@Query("query") query : String) : Call<TvShowResult>

}