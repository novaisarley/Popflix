package com.arley.moviesapp.listener

import com.arley.moviesapp.model.Movie
import com.arley.moviesapp.model.Person
import com.arley.moviesapp.model.TvShow

interface ItemClickListener {
    fun onItemMovieClickListener(movie: Movie)
    fun onItemSerieClickListener(movie: TvShow)
    fun onItemPersonClickListener(person: Person)
}