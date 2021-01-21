package com.arley.moviesapp.ui

import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.arley.moviesapp.Constants
import com.arley.moviesapp.utils.NetworkUtils
import com.arley.moviesapp.R
import com.arley.moviesapp.TMDBServer
import com.arley.moviesapp.listener.ItemClickListener
import com.arley.moviesapp.adapter.MovieSearchAdapter
import com.arley.moviesapp.adapter.ShowsSearchAdapter
import com.arley.moviesapp.listener.ConnectionListener
import com.arley.moviesapp.model.*
import com.arley.moviesapp.utils.Connection
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity(),
    ItemClickListener, ConnectionListener{

    lateinit var moviesRecyclerView: RecyclerView
    lateinit var showsRecyclerView: RecyclerView
    var lastSearch: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        Connection.startVerification(applicationContext, this)

        moviesRecyclerView = activity_search_rv_movies
        showsRecyclerView = activity_search_rv_shows

        setInitialLists()

        val edtSearch: EditText = activity_search_edt_title

        edtSearch.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                if (!edtSearch.text.trim().isEmpty()){
                    if (Connection.isConnectedToNetwork(applicationContext)){
                        searchByTitle(edtSearch.text.toString())
                    }else{
                        Toast.makeText(applicationContext, "Connect to Wifi or mobile data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            false
        }

        edtSearch.requestFocus()

    }

    fun searchByTitle(query: String){
        getMovieSearchByTiltle(query)
        getShowsSearchByTiltle(query)
    }

    fun getMovieSearchByTiltle(query: String) {

        val formatedQuery: String = query.replace(" ", "+")

        val retrofitClient = NetworkUtils
            .getRetrofitInstance(Constants.TMDB_BASE_URL)

        val tmdbServer = retrofitClient.create(TMDBServer::class.java)
        val callback = tmdbServer.getMovieSearchByTiltle(formatedQuery)

        callback.enqueue(object : Callback<MoviesResult> {
            override fun onFailure(call: Call<MoviesResult>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<MoviesResult>, response: Response<MoviesResult>) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                        baseContext,
                        "Error code: " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val moviesResult: MoviesResult = response.body()!!

                var list = moviesResult.results

                var formatedList: MutableList<Movie> = ArrayList()

                list.forEach {
                    if (!it.posterPath.toString().equals("null")) {
                        formatedList.add(it)
                    }
                }

                buildMoviesRecyclerView(formatedList)

            }
        })

    }

    fun getShowsSearchByTiltle(query: String) {

        val formatedQuery: String = query.replace(" ", "+")

        val retrofitClient = NetworkUtils
            .getRetrofitInstance(Constants.TMDB_BASE_URL)

        val tmdbServer = retrofitClient.create(TMDBServer::class.java)
        val callback = tmdbServer.getTvShowsSearchByTiltle(formatedQuery)

        callback.enqueue(object : Callback<TvShowResult> {
            override fun onFailure(call: Call<TvShowResult>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<TvShowResult>, response: Response<TvShowResult>) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                        baseContext,
                        "Error code: " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val showsList: TvShowResult = response.body()!!
                var list = showsList.results
                var formatedList: MutableList<TvShow> = ArrayList()

                list.forEach {
                    if (!it.posterPath.toString().equals("null")) {
                        formatedList.add(it)
                    }
                }
                buildTvShowsRecyclerView(formatedList)

            }
        })

    }

    fun buildMoviesRecyclerView(moviesList: List<Movie>) {
        var spamCount = 3
        if (isTablet()) spamCount = 4 else spamCount = 3
        moviesRecyclerView.adapter = MovieSearchAdapter(moviesList, this, this)
        val layoutManager = StaggeredGridLayoutManager(spamCount, StaggeredGridLayoutManager.VERTICAL)
        moviesRecyclerView.layoutManager = layoutManager
    }

    fun buildTvShowsRecyclerView(showsList: List<TvShow>) {
        var spamCount = 3
        if (isTablet()) spamCount = 4 else spamCount = 3
        showsRecyclerView.adapter = ShowsSearchAdapter(showsList, this, this)
        val layoutManager = StaggeredGridLayoutManager(spamCount, StaggeredGridLayoutManager.VERTICAL)
        showsRecyclerView.layoutManager = layoutManager
    }

    private fun setInitialLists() {
        var initialMoviesList: MutableList<Movie> = ArrayList()
        var initialTvShowsList: MutableList<TvShow> = ArrayList()

        for (i in 0..12) initialMoviesList.add(Movie.createEmptyMovie())
        for (i in 0..12) initialTvShowsList.add(TvShow.createEmptyTvShow())

        buildMoviesRecyclerView(initialMoviesList)
        buildTvShowsRecyclerView(initialTvShowsList)
    }

    fun isTablet() : Boolean{
        try {

            val dm: DisplayMetrics = applicationContext.getResources().getDisplayMetrics();
            val screenWidth : Float = dm.widthPixels / dm.xdpi;
            val screenHeight : Float = dm.heightPixels / dm.ydpi;
            val size : Double = Math.sqrt(Math.pow(screenWidth.toDouble(), 2.0) +
                    Math.pow(screenHeight.toDouble(), 2.0));
            // Tablet devices have a screen size greater than 6 inches
            return size >= 8;
        } catch(t: Throwable ) {
            return false;
        }
    }

    override fun onItemMovieClickListener(movie: Movie) {
        if (Connection.isConnectedToNetwork(applicationContext)){
            val intent : Intent = Intent(applicationContext, MovieSpecificationActivity::class.java)
            intent.putExtra("movie", movie)

            startActivity(intent)
        }else{
            Toast.makeText(applicationContext, "Connect to Wifi or mobile data", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onItemSerieClickListener(tvShow: TvShow) {
        if (Connection.isConnectedToNetwork(applicationContext)){
            val intent : Intent = Intent(applicationContext, MovieSpecificationActivity::class.java)
            intent.putExtra("show", tvShow)

            startActivity(intent)
        }else{
            Toast.makeText(applicationContext, "Connect to Wifi or mobile data", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onItemPersonClickListener(person: Person) {
    }

    override fun onConnectionLost() {
    }

    override fun onConnectionAvailable() {
    }
}