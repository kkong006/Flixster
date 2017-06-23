package com.codepath.flixster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flixster.models.Movie;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MovieDetailsActivity extends AppCompatActivity {

    // Intent Key
    public static final String MOVIE_ID_KEY = "MOVIE_ID";

    // Movie to display
    Movie movie;

    // View objects
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rvVoteAverage) RatingBar rbVoteAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        // Unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // Set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // Vote average is 0...10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);
    }

    @OnClick(R.id.btViewTrailer)
    public void viewTrailer() {
        Intent i = new Intent(this, MovieTrailerActivity.class);
        i.putExtra(MOVIE_ID_KEY, movie.getId());
        startActivity(i);
    }
}
