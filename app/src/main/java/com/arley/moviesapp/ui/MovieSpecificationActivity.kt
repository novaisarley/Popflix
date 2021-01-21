package com.arley.moviesapp.ui

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arley.moviesapp.Constants
import com.arley.moviesapp.utils.NetworkUtils
import com.arley.moviesapp.R
import com.arley.moviesapp.TMDBServer
import com.arley.moviesapp.adapter.CastAdapter
import com.arley.moviesapp.adapter.CrewAdapter
import com.arley.moviesapp.listener.ItemClickListener
import com.arley.moviesapp.model.*
import com.arley.moviesapp.utils.Connection
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_movie_specification.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class MovieSpecificationActivity : AppCompatActivity(),
    ItemClickListener {

    lateinit var tvTitle: TextView
    lateinit var tvRating: TextView
    lateinit var tvOverview: TextView
    lateinit var tvReleaseDate: TextView

    lateinit var ibBack: ImageButton

    lateinit var ivBackdrop: ImageView

    lateinit var ratingBar: RatingBar

    lateinit var rvCast: RecyclerView
    lateinit var rvCrew: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_specification)

        val movie = intent.extras?.getParcelable<Movie>("movie")
        val show = intent.extras?.getParcelable<TvShow>("show")

        tvTitle = tv_title
        tvRating = tv_rating
        tvOverview = tv_overview
        tvReleaseDate = tv_release_date

        ibBack = ib_back
        ivBackdrop = iv_backdrop

        ratingBar = rating_bar

        rvCast = rv_cast
        rvCrew = rv_crew

        ibBack.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                onBackPressed()
            }
        })

        if (Connection.isConnectedToNetwork(applicationContext)) {
            movie?.let {
                setMovieContent(it)
            }

            show?.let {
                setShowContent(it)
            }
        } else {
            Toast.makeText(applicationContext, R.string.connect_to_internet, Toast.LENGTH_SHORT)
                .show()
        }




        setInitialLists()

        tv_title.setMovementMethod(ScrollingMovementMethod())

    }

    fun setMovieContent(movie: Movie) {
        val day: String
        val month: String
        val year: String

        val formatedDate: String

        if (!movie.releaseDate?.trim()?.isEmpty()!!) {
            val dateArray: List<String>? = movie.releaseDate?.split("-")?.toList()
            day = dateArray!!.get(2)
            month = dateArray.get(1)
            year = dateArray.get(0)

            formatedDate = "${day}/${month}/${year}"
            tvReleaseDate.text = formatedDate
        }

        movie.backdropPath.let {
            Glide.with(applicationContext).load(Constants.TMDB_HIGH_IMAGE_BASE_URL + it)
                .into(ivBackdrop)
        }
        movie.backdropPath ?: run {
            Glide.with(applicationContext)
                .load(Constants.TMDB_HIGH_IMAGE_BASE_URL + movie.posterPath).into(ivBackdrop)
        }

        tvTitle.text = movie.title
        tvRating.text = movie.voteAverage.toString()
        tvOverview.text = movie.overview
        ratingBar.rating = movie.voteAverage / 2.0f

        getCreditsByMovie(movie.id)
    }

    fun setShowContent(show: TvShow) {
        val day: String
        val month: String
        val year: String

        val formatedDate: String

        if (!show.firstAirDate?.trim()?.isEmpty()!!) {
            val dateArray: List<String>? = show.firstAirDate?.split("-")?.toList()
            day = dateArray!!.get(2)
            month = dateArray.get(1)
            year = dateArray.get(0)

            formatedDate = "${day}/${month}/${year}"
            tvReleaseDate.text = formatedDate
        }

        show.backdropPath.let {
            Glide.with(applicationContext).load(Constants.TMDB_HIGH_IMAGE_BASE_URL + it)
                .into(ivBackdrop)
        }
        show.backdropPath ?: run {
            Glide.with(applicationContext)
                .load(Constants.TMDB_HIGH_IMAGE_BASE_URL + show.posterPath).into(ivBackdrop)
        }

        tvTitle.text = show.name
        tvRating.text = show.voteAverage.toString()
        tvOverview.text = show.overview
        ratingBar.rating = show.voteAverage / 2.0f

        getCreditsByShow(show.id)
    }

    fun getCreditsByMovie(id: Int) {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance(Constants.TMDB_BASE_URL)

        val language = Locale.getDefault().language + "-" + Locale.getDefault().country

        val tmdbServer = retrofitClient.create(TMDBServer::class.java)
        val callback = tmdbServer.getMovieCredits(id, language)

        callback.enqueue(object : Callback<CreditsResult> {
            override fun onFailure(call: Call<CreditsResult>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<CreditsResult>, response: Response<CreditsResult>) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                        baseContext,
                        "Error: " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val result: CreditsResult = response.body()!!
                val listCrew: List<CrewMember> = result.crew
                val listCast: List<CastMember> = result.cast

                buildCrewRecyclerView(listCrew)
                buildCastRecyclerView(listCast)
            }
        })
    }

    fun getCreditsByShow(id: Int) {
        val retrofitClient = NetworkUtils
            .getRetrofitInstance(Constants.TMDB_BASE_URL)

        val language = Locale.getDefault().language + "-" + Locale.getDefault().country

        val tmdbServer = retrofitClient.create(TMDBServer::class.java)
        val callback = tmdbServer.getTvShowCredits(id, language)

        callback.enqueue(object : Callback<CreditsResult> {
            override fun onFailure(call: Call<CreditsResult>, t: Throwable) {
                Toast.makeText(baseContext, t.message, Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<CreditsResult>, response: Response<CreditsResult>) {
                if (!response.isSuccessful()) {
                    Toast.makeText(
                        baseContext,
                        "Error: " + response.code().toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val result: CreditsResult = response.body()!!
                val listCrew: List<CrewMember> = result.crew
                val listCast: List<CastMember> = result.cast

                buildCrewRecyclerView(listCrew)
                buildCastRecyclerView(listCast)
            }
        })
    }

    fun buildCastRecyclerView(castList: List<CastMember>) {
        rvCast.adapter = CastAdapter(castList, this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvCast.layoutManager = layoutManager
    }

    fun buildCrewRecyclerView(crewList: List<CrewMember>) {
        rvCrew.adapter = CrewAdapter(crewList, this, this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvCrew.layoutManager = layoutManager
    }

    private fun setInitialLists() {
        var initialCrewList: MutableList<CrewMember> = ArrayList()
        var initialCastList: MutableList<CastMember> = ArrayList()

        for (i in 0..4) initialCrewList.add(CrewMember.createEmptyCrewMember())
        for (i in 0..4) initialCastList.add(CastMember.createEmptyCastMember())

        buildCrewRecyclerView(initialCrewList)
        buildCastRecyclerView(initialCastList)
    }

    override fun onItemMovieClickListener(movie: Movie) {
    }

    override fun onItemSerieClickListener(movie: TvShow) {
    }

    override fun onItemPersonClickListener(person: Person) {
    }


}