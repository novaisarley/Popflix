package com.arley.moviesapp.adapter

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arley.moviesapp.Constants
import com.arley.moviesapp.R
import com.arley.moviesapp.listener.ItemClickListener
import com.arley.moviesapp.model.Movie
import com.bumptech.glide.Glide


class MovieAdapter(private val moviesList : List<Movie>, val context: Context, val itemClickListener: ItemClickListener) : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     *
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView
        val ivPoster: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            tvTitle = view.findViewById(R.id.tv_title)
            ivPoster = view.findViewById(R.id.iv_poster)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_media, parent, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val animationDrawable = holder.ivPoster.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(3000)
        animationDrawable.setExitFadeDuration(3000)
        animationDrawable.start()

        holder.ivPoster.clipToOutline = true
        holder.tvTitle.text = moviesList.get(position).title

        if (!isMovieEmpty(moviesList.get(position))){
            holder.ivPoster.setOnClickListener{
                itemClickListener.onItemMovieClickListener(moviesList.get(position))
            }
            Glide.with(context)
                .load(Constants.TMDB_HIGH_IMAGE_BASE_URL+moviesList.get(position).posterPath)
                .into(holder.ivPoster)
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return moviesList.size
    }

    fun isMovieEmpty(movie: Movie):  Boolean{
        if (movie.equals(Movie.createEmptyMovie())){
            return true
        }
        return false
    }
}