package com.west.pratice.shop

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.row_movie.view.*
import org.jetbrains.anko.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.net.URL
import com.west.pratice.shop.MovieActivity.MovieHolder as MovieHolder1

class MovieActivity : AppCompatActivity(), AnkoLogger {

    lateinit var movies: List<Movie>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

//        learnTask()
//        learnUseAnko()
        doAsync {
            learnRetrofit()
            uiThread {
                setRecycler()
            }
        }
    }

    private fun parseGson(json: String?) {
        movies = Gson().fromJson<List<Movie>>(
            json,
            object : TypeToken<List<Movie>>() {}.type
        )
    }

    private fun setRecycler() {
        toast("Got it")
        alert("Got it", "Alert") {
            okButton {
                with(recycler_movie) {
                    layoutManager = LinearLayoutManager(this@MovieActivity)
                    setHasFixedSize(true)
                    adapter = MovieAdater(movies)
                }
            }
        }.show()
    }

    private fun learnRetrofit() {
        val url = "https://api.myjson.com/bins/"
        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(MovieService::class.java)
        movies = service.movieList().execute().body()!!
    }

    private fun learnUseAnko() {
        doAsync {
            val url = "https://api.myjson.com/bins/1727dw"
            val json = URL(url).readText()
            parseGson(json)
            uiThread {
                setRecycler()
            }
        }
    }

    private fun learnTask() {
        val url = "https://api.myjson.com/bins/1727dw"
        MovieTask().execute(url)
    }

    inner class MovieTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            val json = URL(params[0]).readText()
            return json
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            parseGson(result)
            setRecycler()
        }
    }

    inner class MovieAdater(movies: List<Movie>) : BaseAdapter<Movie>(movies) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.row_movie, parent, false)
            return MovieHolder(view)
        }
    }

    inner class MovieHolder(view: View) : BaseAdapter.BaseViewHolder<Movie>(view) {
        val titleText = view.tv_title
        val pictureImg = view.img_picture
        val imdbText = view.tv_imdb
        val directorText = view.tv_director

        override fun onBind(movie: Movie) {
            titleText.text = movie.Title
            imdbText.text = movie.imdbRating
            directorText.text = movie.Director
            Glide.with(this@MovieActivity)
                .load(movie.Images[0])
                .override(300)
                .into(pictureImg)
        }
    }
}


data class Movie(
    val Actors: String,
    val Awards: String,
    val ComingSoon: Boolean,
    val Country: String,
    val Director: String,
    val Genre: String,
    val Images: List<String>,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Rated: String,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Title: String,
    val Type: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String,
    val totalSeasons: String
)

interface MovieService {
    @GET("1727dw")
    fun movieList(): Call<List<Movie>>
}
