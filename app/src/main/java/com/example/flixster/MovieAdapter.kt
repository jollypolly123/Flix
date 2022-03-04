package com.example.flixster

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

const val MOVIE = "movie"
private const val TAG = "MovieAdapter"
class MovieAdapter(private val context: Context, private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder position $position")
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private var ivPoster = itemView.findViewById<ImageView>(R.id.ivPoster)
        private var tvTitle = itemView.findViewById<TextView>(R.id.tvTitle)
        private var tvOverview = itemView.findViewById<TextView>(R.id.tvOverview)
        private var playButton = itemView.findViewById<ImageView>(R.id.playButton)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(movie: Movie) {
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            if (movie.voteAverage > 5) playButton.visibility = View.VISIBLE

            if (context.resources.configuration.orientation == ORIENTATION_LANDSCAPE) {
                Glide.with(context)
                    .load(movie.backdropImageUrl)
                    .transform(RoundedCorners(20))
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(ivPoster)
            } else {
                Glide.with(context)
                    .load(movie.posterImageUrl)
                    .transform(RoundedCorners(20))
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(ivPoster)
            }
        }

        override fun onClick(v: View?) {
            // Notify which movie clicked
            val movie = movies[adapterPosition]
            Toast.makeText(context, movie.title, Toast.LENGTH_SHORT).show()
            // Navigate to new activity using Intent
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(MOVIE, movie)
            context.startActivity(intent)
        }
    }

}
