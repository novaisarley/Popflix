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
import com.arley.moviesapp.model.Person
import com.bumptech.glide.Glide


class PersonAdapter(private val personsList : List<Person>, val context: Context, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView
        val tvAs: TextView
        val ivProfile: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            tvName = view.findViewById(R.id.tv_name)
            tvAs = view.findViewById(R.id.tv_as)
            ivProfile = view.findViewById(R.id.iv_profile)
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_person, parent, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val animationDrawable = holder.ivProfile.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(3000)
        animationDrawable.start()

        holder.ivProfile.clipToOutline = true
        holder.tvName.text = personsList.get(position).name
        holder.tvAs.visibility = View.GONE
        holder.ivProfile.setOnClickListener{
            itemClickListener.onItemPersonClickListener(personsList.get(position))
        }
        Glide.with(context)
            .load(Constants.TMDB_LOW_IMAGE_BASE_URL+personsList.get(position).profilePath)
            .into(holder.ivProfile)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return personsList.size
    }
}