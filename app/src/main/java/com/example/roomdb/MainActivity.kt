package com.example.roomdb

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdb.room.Movie
import com.example.roomdb.room.MovieDB
import com.example.roomdb.room.constant
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    val db by lazy { MovieDB(this) }
    lateinit var movieAdapter: MovieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListener()
        setupRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        loadMovie()
    }

    fun loadMovie() {
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.movieDao().getMovies()
            Log.d("MainActivity", "dbResponse: $movies")
            withContext(Dispatchers.Main) {
                movieAdapter.setdata(movies)
            }
        }
    }

    fun setupListener() {
        button_create.setOnClickListener {
            intentEdit(0, constant.TYPE_CREATE)
        }
    }

    fun intentEdit(movieId: Int, intentType: Int) {
        startActivity(
            Intent(applicationContext, EditActivity::class.java)
                .putExtra("intent_id", movieId)
                .putExtra("intent_type", intentType)
        )
    }


    private fun setupRecyclerView() {
        movieAdapter = MovieAdapter(arrayListOf(), object : MovieAdapter.onAdapterListener {
            override fun onClick(movie: Movie) {
                // read detail movie
                intentEdit(movie.id, constant.TYPE_READ)
            }

            override fun onUpdate(movie: Movie) {
                intentEdit(movie.id, constant.TYPE_UPDATE)
            }

            override fun onDelete(movie: Movie) {
                deleteDialog(movie)
            }

        })
        list_food.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = movieAdapter
        }
    }

    private fun deleteDialog(movie: Movie) {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("konfirmasi")
            setMessage("yakin hapus ${movie.title}?")
            setNegativeButton("Batal") { dialogInterface, i ->
                dialogInterface.dismiss()
            }
            setPositiveButton("Hapus") { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.movieDao().deleteMovie(movie)
                    loadMovie()
                }
            }
        }
        alertDialog.show()
    }
}