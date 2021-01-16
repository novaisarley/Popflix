package com.arley.moviesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TableLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arley.moviesapp.Constants
import com.arley.moviesapp.NetworkUtils
import com.arley.moviesapp.R
import com.arley.moviesapp.TMDBServer
import com.arley.moviesapp.adapter.ItemClickListener
import com.arley.moviesapp.adapter.MovieAdapter
import com.arley.moviesapp.adapter.ShowsAdapter
import com.arley.moviesapp.model.Movie
import com.arley.moviesapp.model.MoviesResult
import com.arley.moviesapp.model.TvShow
import com.arley.moviesapp.model.TvShowResult
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), ItemClickListener {

    lateinit var moviesRecyclerView : RecyclerView
    lateinit var showsRecyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pb_popular_movies.visibility = View.VISIBLE
        pb_popular_tvshows.visibility = View.VISIBLE

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
                    pb_popular_movies.visibility = View.GONE


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

        callback.enqueue(object : Callback<TvShowResult> {
            override fun onFailure(call: Call<TvShowResult>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<TvShowResult>, response: Response<TvShowResult>) {
                if(!response.isSuccessful()){
                    Toast.makeText(baseContext, "Error code: "+response.code().toString(), Toast.LENGTH_SHORT).show()
                    pb_popular_tvshows.visibility = View.GONE
                    return
                }
                val showsResult : TvShowResult = response.body()!!
                buildShowsRecyclerView(showsResult.results)

            }
        })

    }

    fun buildMoviesRecyclerView(moviesList : List<Movie>){
        moviesRecyclerView.adapter = MovieAdapter( moviesList,this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        moviesRecyclerView.layoutManager = layoutManager
        pb_popular_movies.visibility = View.GONE
    }

    fun buildShowsRecyclerView(tvShowsList : List<TvShow>){
        showsRecyclerView.adapter = ShowsAdapter( tvShowsList,this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//      val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        showsRecyclerView.layoutManager = layoutManager
        pb_popular_tvshows.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onItemMovieClickListener(movie: Movie) {
        Toast.makeText(this, movie.originalTitle, Toast.LENGTH_SHORT).show()
    }

    override fun onItemSerieClickListener(tvShow: TvShow) {
        Toast.makeText(this, tvShow.originalname, Toast.LENGTH_SHORT).show()
    }
}