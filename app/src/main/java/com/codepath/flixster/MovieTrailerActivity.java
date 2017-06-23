package com.codepath.flixster;

import android.os.Bundle;
import android.util.Log;

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

import cz.msebera.android.httpclient.Header;

import static com.codepath.flixster.MovieDetailsActivity.MOVIE_ID_KEY;
import static com.codepath.flixster.MovieListActivity.API_BASE_URL;
import static com.codepath.flixster.MovieListActivity.API_KEY_PARAM;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    public static final String MOVIE_ID_PARAM = "movie_id";

    AsyncHttpClient client;
    // Movie id for URL
    int movieId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);

        // Initialize the client
        client = new AsyncHttpClient();

        movieId = getIntent().getIntExtra(MOVIE_ID_KEY, 0);

        getVideos();

        // Resolve the player view from the layout
        YouTubePlayerView playerView = (YouTubePlayerView) findViewById(R.id.player);

//        // Initialize with API key stored in secrets.xml
//        playerView.initialize(getString(R.string.youtube_api_key), new YouTubePlayer.OnInitializedListener() {
//            @Override
//            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
//                // Do work to cue, play video
//                youTubePlayer.cueVideo(videoId);
//            }
//
//            @Override
//            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
//                // Log the error
//                Log.e("MovieTrailerActivity", "Error initializing YouTube player");
//            }
//        });
    }

    // TODO - Move this into the list activity
    // Get the movie's videos
    private void getVideos() {
        // Create the URL
        String url = API_BASE_URL + "/movie/" + movieId + "/videos";
        // Send the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API key
        params.put(MOVIE_ID_KEY, movieId); // Movie id
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray results = null;
                try {
                    results = response.getJSONArray("results");
                    // If the results are valid, fetch the video
                    if(results.length() > 0) {
                        Video video = new Video(results.getJSONObject(0));
                        // Get the Youtube key from the video
                        final String videoId = video.getKey();
                        if(video.getSite().equals("YouTube")) {
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
                                    Log.e("MovieTrailerActivity", "Error initializing YouTube player");
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    Log.e("MovieTrailerActivity", "Failed to get data from now_playing endpoint " + e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("MovieTrailerActivity", "Failed to get data from now_playing endpoint");
            }
        });
    }
    // Get the list of currently playing movies from the API
//    private void getNowPlaying() {
//        // Create the URL
//        String url = API_BASE_URL + "/movie/now_playing";
//        // Set the request parameters
//        RequestParams params = new RequestParams();
//        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API key, always required
//        client.get(url, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                // Load the result into movies list
//                try {
//                    JSONArray results = response.getJSONArray("results");
//                    // Iterate through the result set and create Movie objects
//                    for(int i = 0; i < results.length(); i++) {
//                        Movie movie = new Movie(results.getJSONObject(i));
//                        movies.add(movie);
//                        // Notify adapter that a row was added
//                        adapter.notifyItemInserted(movies.size() - 1);
//                    }
//                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
//                } catch (JSONException e) {
//                    logError("Failed to parse now playing movies", e, true);
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                logError("Failed to get data from now_playing endpoint", throwable, true);
//            }
//        });
//    }
}