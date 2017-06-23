package com.codepath.flixster;

import android.os.Bundle;
import android.util.Log;
import android.widget.RatingBar;
import android.widget.TextView;

import com.codepath.flixster.models.Movie;
import com.codepath.flixster.models.Video;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static com.codepath.flixster.MovieListActivity.API_BASE_URL;
import static com.codepath.flixster.MovieListActivity.API_KEY_PARAM;

public class MovieDetailsActivity extends YouTubeBaseActivity {

    // Intent Keys
    public static final String MOVIE_ID_KEY = "MOVIE_ID";

    // Class TAG
    private final String TAG = "MovieDetailsActivity";

    // Movie to display
    Movie movie;

    String videoId;
    String genre;

    AsyncHttpClient client;

    // View objects
    @BindView(R.id.tvTitle) TextView tvTitle;
    @BindView(R.id.tvOverview) TextView tvOverview;
    @BindView(R.id.rvVoteAverage) RatingBar rbVoteAverage;
    @BindView(R.id.tvReleaseDate) TextView tvReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        // Initialize the client
        client = new AsyncHttpClient();

        // Unwrap the movie passed in via intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        if(movie.getReleaseDate() != null) {
            tvReleaseDate.setText(movie.getReleaseDate().substring(0, 4));
        }

        Log.d(TAG, String.format("Showing details for '%s'", movie.getTitle()));

        // Set the title and overview
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        // Vote average is 0...10, convert to 0..5 by dividing by 2
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        // Get the video id
        getVideos();
    }

    private void getVideos() {
        // Create the URL
        String url = API_BASE_URL + "/movie/" + movie.getId() + "/videos";

        // Send the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API key
        params.put(MOVIE_ID_KEY, movie.getId());
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray results = null;
                try {
                    results = response.getJSONArray("results");
                    // Get the first valid YouTube video result
                    for(int i = 0; i < results.length(); i++) {
                        Video video = new Video(results.getJSONObject(i));
                        if(video.getSite().equals("YouTube")) {
                            videoId = video.getKey();
                            showVideo();
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e(TAG, "Failed to get data from not_playing endpoint");
            }
        });
    }

    private void showVideo() {
        // Resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

        // Initialize with API key stored in secrets.xml
        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                // Do work to cue, play video
                youTubePlayer.cueVideo(videoId);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                // Log the error
                Log.e(TAG, "Error initializing YouTube player");
            }
        });
    }
}
