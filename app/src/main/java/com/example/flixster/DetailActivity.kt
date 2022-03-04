package com.example.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers

private const val YOUTUBE_API_KEY = "AIzaSyCKU4mbtxKVag2Scd_zQvmVfwlpg_3eAzU"
private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val TAG = "DetailActivity"
class DetailActivity : YouTubeBaseActivity() {

    private lateinit var ytPlayerView: YouTubePlayerView
    private lateinit var tvTitle: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var tvOverview: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        tvTitle = findViewById(R.id.tvTitle)
        ratingBar = findViewById(R.id.rbVoteAverage)
        tvOverview = findViewById(R.id.tvOverview)
        ytPlayerView = findViewById(R.id.ytPlayerView)

        val movie = intent.getParcelableExtra<Movie>(MOVIE) as Movie
        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        ratingBar.rating = movie.voteAverage.toFloat()

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieId), object: JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess")
                val results = json.jsonObject.getJSONArray("results")
                if (results.length() == 0) {
                    Log.w(TAG, "No movie trailer")
                    return
                }
                val movieTrailerJson = results.getJSONObject(0)
                val youtubeKey = movieTrailerJson.getString("key")

                initializeYouTube(youtubeKey)
            }

        })

    }

    private fun initializeYouTube(youtubeKey: String) {
        ytPlayerView.initialize(YOUTUBE_API_KEY, object: YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i(TAG, "onInitializationSuccess")
                if (ratingBar.rating > 5) {
                    player?.loadVideo(youtubeKey)
                } else {
                    player?.cueVideo(youtubeKey)
                }
            }

            override fun onInitializationFailure(
                provider: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.e(TAG, "onInitializationFailure")
            }

        })

    }
}