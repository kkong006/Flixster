package com.codepath.flixster;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.flixster.models.Config;
import com.codepath.flixster.models.Movie;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by kkong on 6/21/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    // List of movies
    ArrayList<Movie> movies;
    // Config needed for image urls
    Config config;
    // Context for rendering
    Context context;

    // Initialize with list
    public MovieAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    // Creates and inflates a new view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the context and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        // Create the view using the item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        // Return a new ViewHolder
        return new ViewHolder(movieView);
    }

    // Binds an inflated view to a new item
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the movie data at the specified position
        Movie movie = movies.get(position);
        // Populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        // Build URL for poster image
        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());

        // Load image using Glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(holder.ivPosterImage);

    }

    // Returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // Create the viewholder as a static inner class
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // Track view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView) {
            super(itemView);
            // Lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            tvOverview = (TextView) itemView.findViewById(R.id.tvOverview);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
        }
    }
}
