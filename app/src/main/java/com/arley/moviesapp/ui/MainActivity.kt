package com.arley.moviesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arley.moviesapp.Constants
import com.arley.moviesapp.NetworkUtils
import com.arley.moviesapp.R
import com.arley.moviesapp.TMDBServer
import com.arley.moviesapp.adapter.ItemClickListener
import com.arley.moviesapp.adapter.MovieAdapter
import com.arley.moviesapp.adapter.PersonAdapter
import com.arley.moviesapp.adapter.ShowsAdapter
import com.arley.moviesapp.model.*
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity(), ItemClickListener {

    lateinit var popularMoviesRecyclerView : RecyclerView
    lateinit var topRatedMoviesRecyclerView : RecyclerView
    lateinit var showsRecyclerView : RecyclerView
    lateinit var personRecyclerView : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        popularMoviesRecyclerView = rv_popular_movies
        topRatedMoviesRecyclerView = rv_top_rated_movies
        showsRecyclerView = rv_shows
        personRecyclerView = rv_person

        setInitialLists()
        getPopularMovies()
        getPopularShows()
        getTopRatedMovies()
        getTrendingPeople()

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
                buildPopularMoviesRecyclerView(moviesResult.results)

            }
        })

    }

    fun getTopRatedMovies() {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance(Constants.TMDB_BASE_URL)

        val tmdbServer = retrofitClient.create(TMDBServer::class.java)
        val callback = tmdbServer.getTopRatedMovies()

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
                buildTopRatedMoviesRecyclerView(moviesResult.results)

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
                    return
                }
                val showsResult : TvShowResult = response.body()!!
                buildShowsRecyclerView(showsResult.results)

            }
        })

    }

    fun getTrendingPeople() {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance(Constants.TMDB_BASE_URL)

        val tmdbServer = retrofitClient.create(TMDBServer::class.java)
        val callback = tmdbServer.getTrendingPeople()

        callback.enqueue(object : Callback<PeopleResult> {
            override fun onFailure(call: Call<PeopleResult>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<PeopleResult>, response: Response<PeopleResult>) {
                if(!response.isSuccessful()){
                    Toast.makeText(baseContext, "Error code: "+response.code().toString(), Toast.LENGTH_SHORT).show()
                    return
                }
                val showsResult : PeopleResult = response.body()!!
                buildPersonRecyclerView(showsResult.results)

            }
        })

    }

    fun buildPopularMoviesRecyclerView(moviesList : List<Movie>){
        popularMoviesRecyclerView.adapter = MovieAdapter( moviesList,this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        popularMoviesRecyclerView.layoutManager = layoutManager
    }

    fun buildTopRatedMoviesRecyclerView(moviesList : List<Movie>){
        topRatedMoviesRecyclerView.adapter = MovieAdapter( moviesList,this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        topRatedMoviesRecyclerView.layoutManager = layoutManager
    }

    fun buildShowsRecyclerView(tvShowsList : List<TvShow>){
        showsRecyclerView.adapter = ShowsAdapter( tvShowsList,this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//      val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        showsRecyclerView.layoutManager = layoutManager
    }

    fun buildPersonRecyclerView(personList : List<Person>){
        personRecyclerView.adapter = PersonAdapter( personList,this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        personRecyclerView.layoutManager = layoutManager
    }

    private fun setInitialLists() {
        var initialMoviesList : MutableList<Movie> = ArrayList()
        var initialTvShowsList : MutableList<TvShow> = ArrayList()
        var initialPersonList : MutableList<Person> = ArrayList()

        for(i in 0..10) initialMoviesList.add(Movie.createEmptyMovie())
        for(i in 0..10) initialTvShowsList.add(TvShow.createEmptyTvShow())
        for(i in 0..10) initialPersonList.add(Person.createEmptyPerson())

        buildPopularMoviesRecyclerView(initialMoviesList)
        buildShowsRecyclerView(initialTvShowsList)
        buildPersonRecyclerView(initialPersonList)
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

    override fun onItemPersonClickListener(person: Person) {
        Toast.makeText(this, person.name, Toast.LENGTH_SHORT).show()
    }
}