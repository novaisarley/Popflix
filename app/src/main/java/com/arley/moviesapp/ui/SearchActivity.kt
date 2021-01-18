package com.arley.moviesapp.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.arley.moviesapp.Constants
import com.arley.moviesapp.NetworkUtils
import com.arley.moviesapp.R
import com.arley.moviesapp.TMDBServer
import com.arley.moviesapp.adapter.ItemClickListener
import com.arley.moviesapp.adapter.MovieSearchAdapter
import com.arley.moviesapp.adapter.ShowsSearchAdapter
import com.arley.moviesapp.model.*
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity(), ItemClickListener {

    lateinit var moviesRecyclerView: RecyclerView
    lateinit var showsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        moviesRecyclerView = activity_search_rv_movies
        showsRecyclerView = activity_search_rv_shows

        setInitialLists()

        val edtSearch: EditText = activity_search_edt_title
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        edtSearch.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE) {
                getMovieSearchByTiltle(edtSearch.text.toString())
                getShowsSearchByTiltle(edtSearch.text.toString())
            }
            false
        }

        edtSearch.requestFocus()

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
        moviesRecyclerView.adapter = MovieSearchAdapter(moviesList, this, this)
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        moviesRecyclerView.layoutManager = layoutManager
    }

    fun buildTvShowsRecyclerView(showsList: List<TvShow>) {
        showsRecyclerView.adapter = ShowsSearchAdapter(showsList, this, this)
        val layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
        showsRecyclerView.layoutManager = layoutManager
    }

    private fun setInitialLists() {
        var initialMoviesList: MutableList<Movie> = ArrayList()
        var initialTvShowsList: MutableList<TvShow> = ArrayList()

        for (i in 0..10) initialMoviesList.add(Movie.createEmptyMovie())
        for (i in 0..10) initialTvShowsList.add(TvShow.createEmptyTvShow())

        buildMoviesRecyclerView(initialMoviesList)
        buildTvShowsRecyclerView(initialTvShowsList)
    }

    override fun onItemMovieClickListener(movie: Movie) {

    }

    override fun onItemSerieClickListener(movie: TvShow) {

    }

    override fun onItemPersonClickListener(person: Person) {
    }
}