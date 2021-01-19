package com.arley.moviesapp.adapter

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arley.moviesapp.Constants
import com.arley.moviesapp.R
import com.arley.moviesapp.model.CastMember
import com.arley.moviesapp.model.CrewMember
import com.arley.moviesapp.model.Person
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.item_person.*


class CastAdapter(private val castList : List<CastMember>, val context: Context, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<CastAdapter.ViewHolder>() {
    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView
        val ivProfile: ImageView

        init {
            // Define click listener for the ViewHolder's View.
            tvName = view.findViewById(R.id.tv_name)
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
        holder.tvName.text = castList.get(position).originalName

        castList.get(position).profilePath?.let {
            Glide.with(context)
                .load(Constants.TMDB_LOW_IMAGE_BASE_URL+castList.get(position).profilePath)
                .into(holder.ivProfile)
        } ?: run{
            holder.ivProfile.setImageResource(R.drawable.not_found_profile_img)
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int {
        return castList.size
    }
}