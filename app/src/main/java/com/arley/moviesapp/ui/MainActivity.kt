package com.arley.moviesapp.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.DisplayMetrics
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arley.moviesapp.Constants
import com.arley.moviesapp.R
import com.arley.moviesapp.TMDBServer
import com.arley.moviesapp.adapter.MovieAdapter
import com.arley.moviesapp.adapter.PersonAdapter
import com.arley.moviesapp.adapter.ShowsAdapter
import com.arley.moviesapp.listener.ConnectionListener
import com.arley.moviesapp.listener.ItemClickListener
import com.arley.moviesapp.model.*
import com.arley.moviesapp.utils.Connection
import com.arley.moviesapp.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(),
    ItemClickListener, ConnectionListener {

    lateinit var popularMoviesRecyclerView: RecyclerView
    lateinit var topRatedMoviesRecyclerView: RecyclerView
    lateinit var showsRecyclerView: RecyclerView
    lateinit var personRecyclerView: RecyclerView
    lateinit var layoutStatusConnection: LinearLayout
    lateinit var tvStatusConnection: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Connection.startVerification(applicationContext, this)

        popularMoviesRecyclerView = rv_popular_movies
        topRatedMoviesRecyclerView = rv_top_rated_movies
        showsRecyclerView = rv_shows
        personRecyclerView = rv_person
        tvStatusConnection = tv_connection_status
        layoutStatusConnection = layout_connection_status

        setInitialLists()

        bt_search.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(Intent(applicationContext, SearchActivity::class.java))
            }
        })

        if (Connection.isConnectedToNetwork(applicationContext)) {
            populateRecyclerViews()
        } else {
            Toast.makeText(applicationContext, getString(R.string.connect_to_internet), Toast.LENGTH_LONG).show()
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
    }

    private fun populateRecyclerViews() {
        getPopularMovies()
        getPopularShows()
        getTopRatedMovies()
        getTrendingPeople()
    }

    fun getPopularMovies() {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance(Constants.TMDB_BASE_URL)

        val language = Locale.getDefault().language + "-" + Locale.getDefault().country

        val tmdbServer = retrofitClient.create(TMDBServer::class.java)
        val callback = tmdbServer.getPopularMovies(language)

        callback.enqueue(object : Callback<MoviesResult> {
            override fun onFailure(call: Call<MoviesResult>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MoviesResult>, response: Response<MoviesResult>) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                        baseContext,
                        "Error: " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val moviesResult: MoviesResult = response.body()!!

                var list: MutableList<Movie> = moviesResult.results
                list.shuffle()

                buildPopularMoviesRecyclerView(list)

            }
        })

    }

    fun getTopRatedMovies() {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance(Constants.TMDB_BASE_URL)

        val language = Locale.getDefault().language + "-" + Locale.getDefault().country

        val tmdbServer = retrofitClient.create(TMDBServer::class.java)
        val callback = tmdbServer.getTopRatedMovies(language)

        callback.enqueue(object : Callback<MoviesResult> {
            override fun onFailure(call: Call<MoviesResult>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MoviesResult>, response: Response<MoviesResult>) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                        baseContext,
                        "Error: " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val moviesResult: MoviesResult = response.body()!!

                var list: MutableList<Movie> = moviesResult.results
                list.shuffle()

                buildTopRatedMoviesRecyclerView(list)
            }
        })

    }

    fun getPopularShows() {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance(Constants.TMDB_BASE_URL)

        val language = Locale.getDefault().language + "-" + Locale.getDefault().country

        val tmdbServer = retrofitClient.create(TMDBServer::class.java)
        val callback = tmdbServer.getPopularShows(language)

        callback.enqueue(object : Callback<TvShowResult> {
            override fun onFailure(call: Call<TvShowResult>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<TvShowResult>, response: Response<TvShowResult>) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                        baseContext,
                        "Error: " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val showsResult: TvShowResult = response.body()!!

                var list: MutableList<TvShow> = showsResult.results
                list.shuffle()

                buildShowsRecyclerView(list)

            }
        })

    }

    fun getTrendingPeople() {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance(Constants.TMDB_BASE_URL)

        val language = Locale.getDefault().language + "-" + Locale.getDefault().country

        val tmdbServer = retrofitClient.create(TMDBServer::class.java)
        val callback = tmdbServer.getTrendingPeople(language)

        callback.enqueue(object : Callback<PeopleResult> {
            override fun onFailure(call: Call<PeopleResult>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<PeopleResult>, response: Response<PeopleResult>) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                        baseContext,
                        "Error: " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val showsResult: PeopleResult = response.body()!!

                var list = showsResult.results

                var formatedList: MutableList<Person> = ArrayList()

                list.forEach {
                    if (!it.profilePath.toString().equals("null")) {
                        formatedList.add(it)
                    }
                }

                formatedList.shuffle()

                buildPersonRecyclerView(formatedList)

            }
        })

    }

    fun buildPopularMoviesRecyclerView(moviesList: List<Movie>) {
        popularMoviesRecyclerView.adapter = MovieAdapter(moviesList, this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        popularMoviesRecyclerView.layoutManager = layoutManager
    }

    fun buildTopRatedMoviesRecyclerView(moviesList: List<Movie>) {
        topRatedMoviesRecyclerView.adapter = MovieAdapter(moviesList, this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        topRatedMoviesRecyclerView.layoutManager = layoutManager
    }

    fun buildShowsRecyclerView(tvShowsList: List<TvShow>) {
        showsRecyclerView.adapter = ShowsAdapter(tvShowsList, this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//      val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        showsRecyclerView.layoutManager = layoutManager
    }

    fun buildPersonRecyclerView(personList: List<Person>) {
        personRecyclerView.adapter = PersonAdapter(personList, this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        personRecyclerView.layoutManager = layoutManager
    }

    private fun setInitialLists() {
        var initialMoviesList: MutableList<Movie> = ArrayList()
        var initialTvShowsList: MutableList<TvShow> = ArrayList()
        var initialTopRatedMoviesList: MutableList<Movie> = ArrayList()
        var initialPersonList: MutableList<Person> = ArrayList()

        for (i in 0..10) initialMoviesList.add(Movie.createEmptyMovie())
        for (i in 0..10) initialTopRatedMoviesList.add(Movie.createEmptyMovie())
        for (i in 0..10) initialTvShowsList.add(TvShow.createEmptyTvShow())
        for (i in 0..10) initialPersonList.add(Person.createEmptyPerson())

        buildPopularMoviesRecyclerView(initialMoviesList)
        buildTopRatedMoviesRecyclerView(initialTopRatedMoviesList)
        buildShowsRecyclerView(initialTvShowsList)
        buildPersonRecyclerView(initialPersonList)
    }

    override fun onItemMovieClickListener(movie: Movie) {
        if (Connection.isConnectedToNetwork(applicationContext)) {
            val intent: Intent = Intent(applicationContext, MovieSpecificationActivity::class.java)
            intent.putExtra("movie", movie)

            startActivity(intent)
        } else {
            Toast.makeText(applicationContext, R.string.connect_to_internet, Toast.LENGTH_SHORT)
                .show()
        }

    }

    override fun onItemSerieClickListener(tvShow: TvShow) {
        if (Connection.isConnectedToNetwork(applicationContext)) {
            val intent: Intent = Intent(applicationContext, MovieSpecificationActivity::class.java)
            intent.putExtra("show", tvShow)

            startActivity(intent)
        } else {
            Toast.makeText(applicationContext, R.string.connect_to_internet, Toast.LENGTH_SHORT)
                .show()
        }

    }


    override fun onItemPersonClickListener(person: Person) {
        Toast.makeText(this, person.name, Toast.LENGTH_SHORT).show()
    }

    override fun onConnectionLost() {
        Toast.makeText(this, R.string.disconnected, Toast.LENGTH_SHORT).show()
    }

    override fun onConnectionAvailable() {
        populateRecyclerViews()

    }
}