package com.example.roomdb

import android.location.GnssAntennaInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb.room.Movie
import kotlinx.android.synthetic.main.adapter_movie.view.*

class MovieAdapter (private val movies: ArrayList<Movie>, private val listener: onAdapterListener)
    : RecyclerView.Adapter<MovieAdapter.MovieViewolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewolder {
        return MovieViewolder(
            LayoutInflater.from(parent.context).inflate( R.layout.adapter_movie, parent, false)
        )
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MovieViewolder, position: Int) {
        val movie = movies[position]
        holder.view.text_title.text = movie.title
        holder.view.text_title.setOnClickListener {
            listener.onClick( movie)
        }
        holder.view.icon_edit.setOnClickListener {
            listener.onUpdate( movie)
        }
        holder.view.icon_delete.setOnClickListener {
            listener.onDelete(movie)
        }
    }

    class MovieViewolder(val view: View) : RecyclerView.ViewHolder(view)

    fun setdata(list: List<Movie>){
        movies.clear()
        movies.addAll(list)
        notifyDataSetChanged()
    }

    interface onAdapterListener {
        fun onClick(movie: Movie)
        fun onUpdate(movie: Movie)
        fun onDelete(movie: Movie)
    }
}