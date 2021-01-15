package com.arley.moviesapp.adapter

import com.arley.moviesapp.model.Movie
import com.arley.moviesapp.model.TvShow

interface ItemClickListener {
    fun onItemMovieClickListener(movie: Movie)
    fun onItemSerieClickListener(movie: TvShow)
}