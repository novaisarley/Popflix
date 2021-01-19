package com.arley.moviesapp.adapter

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.arley.moviesapp.Constants
import com.arley.moviesapp.R
import com.arley.moviesapp.listener.ItemClickListener
import com.arley.moviesapp.model.TvShow
import com.bumptech.glide.Glide


class ShowsSearchAdapter(private val tvShowsList : List<TvShow>, val context: Context, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<ShowsSearchAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivPoster: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            ivPoster = view.findViewById(R.id.search_item_iv_poster)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_media, parent, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animationDrawable = holder.ivPoster.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(3000)
        animationDrawable.start()

        holder.ivPoster.clipToOutline = true
        if (!isShowEmpty(tvShowsList.get(position))){
            holder.ivPoster.setOnClickListener{
                itemClickListener.onItemSerieClickListener(tvShowsList.get(position))
            }
            Glide.with(context)
                .load(Constants.TMDB_HIGH_IMAGE_BASE_URL+tvShowsList.get(position).posterPath)
                .into(holder.ivPoster)
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return tvShowsList.size
    }

    fun isShowEmpty(show: TvShow):  Boolean{
        if (show.equals(TvShow.createEmptyTvShow())){
            return true
        }
        return false
    }
}