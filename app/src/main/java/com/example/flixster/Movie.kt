package com.example.flixster

import org.json.JSONArray

data class Movie (
    val movieId: Int,
    val title: String,
    private val posterPath: String,
    private val backdropPath: String,
    val overview: String
) {
    val posterImageUrl = "https://image.tmdb.org/t/p/w342/$posterPath"
    val backdropImageUrl = "https://image.tmdb.org/t/p/w342/$backdropPath"
    companion object {
        fun fromJsonArray(json: JSONArray): List<Movie> {
            val movies = mutableListOf<Movie>()
            for (i in 0 until json.length()) {
                val movieJson = json.getJSONObject(i)
                movies.add(
                    Movie(
                        movieJson.getInt("id"),
                        movieJson.getString("title"),
                        movieJson.getString("poster_path"),
                        movieJson.getString("backdrop_path"),
                        movieJson.getString("overview"),
                    )
                )
            }
            return movies
        }
    }
}