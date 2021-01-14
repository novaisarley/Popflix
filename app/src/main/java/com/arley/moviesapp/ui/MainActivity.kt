package com.arley.moviesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.arley.moviesapp.Constants
import com.arley.moviesapp.NetworkUtils
import com.arley.moviesapp.R
import com.arley.moviesapp.TMDBServer
import com.arley.moviesapp.model.Movie
import com.arley.moviesapp.model.MoviesResult
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPopularMovies()
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
                buildRecyclerView(moviesResult.results)

            }
        })

    }

    fun buildRecyclerView(moviesList : List<Movie>){
        textView.text = ""
        moviesList.forEach{
            textView.text = textView.text.toString().plus(it.toString())
        }
    }

    override fun onResume() {
        super.onResume()
    }
}