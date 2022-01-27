package com.example.roomdb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.roomdb.room.Movie
import com.example.roomdb.room.MovieDB
import com.example.roomdb.room.constant
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    val db by lazy { MovieDB(this) }
    private var movieId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupListener()


    }

    fun setupView() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType) {
            constant.TYPE_CREATE -> {
                button_update.visibility = View.GONE
            }
            constant.TYPE_READ -> {
                button_save.visibility = View.GONE
                button_update.visibility = View.GONE
                getmovie()
            }
            constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getmovie()
            }
        }
    }

    private fun getmovie() {
        TODO("Not yet implemented")
    }


    fun setupListener() {
        button_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.movieDao().addMovie(
                    Movie(0, edit_title.text.toString(), edit_title.text.toString())
                )
                finish()
            }
            button_update.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    db.movieDao().updateMovie(
                        Movie(movieId, edit_title.text.toString(), edit_title.text.toString())
                    )
                    finish()
                }

                fun getMovie() {
                    movieId = intent.getIntExtra("intent_id", 0)
                    CoroutineScope(Dispatchers.IO).launch {
                        val movies = db.movieDao().getMovie(movieId)[0]
                        edit_title.setText(movies.title)
                        edit_desc.setText(movies.desc)
                    }
                }

                fun onSupportNavigateUp(): Boolean {
                    onBackPressed()
                    return super.onSupportNavigateUp()
                }
            }
        }
    }
}