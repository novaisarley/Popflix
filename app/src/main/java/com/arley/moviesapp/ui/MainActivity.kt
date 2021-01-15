package com.arley.moviesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.arley.moviesapp.Constants
import com.arley.moviesapp.NetworkUtils
import com.arley.moviesapp.R
import com.arley.moviesapp.TMDBServer
import com.arley.moviesapp.adapter.MovieAdapter
import com.arley.moviesapp.adapter.ShowsAdapter
import com.arley.moviesapp.model.Movie
import com.arley.moviesapp.model.MoviesResult
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    lateinit var moviesRecyclerView : RecyclerView
    lateinit var showsRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //imageView.clipToOutline = true
        moviesRecyclerView = rv_movies
        showsRecyclerView = rv_shows

        getPopularMovies()
        getPopularShows()

    }

    fun getPopularMovies() {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance(Constants.TMDB_BASE_URL)

        val tmdbServer = retrofitClient.create(TMDBServer::class.java)
        val callback = tmdbServer.getPopularMovies()

        callback.enqueue(object : Callback<MoviesResult> {
            override fun onFailure(call: Call<MoviesResult>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MoviesResult>, response: Response<MoviesResult>) {
                if(!response.isSuccessful()){
                    Toast.makeText(baseContext, "Error code: "+response.code().toString(), Toast.LENGTH_SHORT).show()
                    return
                }
                val moviesResult : MoviesResult = response.body()!!
                buildMoviesRecyclerView(moviesResult.results)

            }
        })

    }

    fun getPopularShows() {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance(Constants.TMDB_BASE_URL)

        val tmdbServer = retrofitClient.create(TMDBServer::class.java)
        val callback = tmdbServer.getPopularShows()

        callback.enqueue(object : Callback<MoviesResult> {
            override fun onFailure(call: Call<MoviesResult>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MoviesResult>, response: Response<MoviesResult>) {
                if(!response.isSuccessful()){
                    Toast.makeText(baseContext, "Error code: "+response.code().toString(), Toast.LENGTH_SHORT).show()
                    return
                }
                val moviesResult : MoviesResult = response.body()!!
                buildShowsRecyclerView(moviesResult.results)

            }
        })

    }

    fun buildMoviesRecyclerView(moviesList : List<Movie>){
        moviesRecyclerView.adapter = MovieAdapter( moviesList,this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        moviesRecyclerView.layoutManager = layoutManager
    }

    fun buildShowsRecyclerView(moviesList : List<Movie>){
        showsRecyclerView.adapter = ShowsAdapter( moviesList,this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        showsRecyclerView.layoutManager = layoutManager
    }

    override fun onResume() {
        super.onResume()
    }
}