package com.codepath.flixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.codepath.flixster.models.Config;
import com.codepath.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    // Constants

    // Base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // Parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // Tag for logging from this activity
    public final static String TAG = "MovieListActivity";

    // Instance fields
    AsyncHttpClient client;

    // List of currently playing movies
    ArrayList<Movie> movies;
    // Recycler View
    @BindView(R.id.rvMovies) RecyclerView rvMovies;
    // Adapter wired to the recycler view
    MovieAdapter adapter;
    // Image config
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        ButterKnife.bind(this);
        // Initialize the client
        client = new AsyncHttpClient();
        // Initialize the list of movies
        movies = new ArrayList<>();
        // Initialize the adapter - movies cannot be reinitialized after this point
        adapter = new MovieAdapter(movies);

        // Resolve the recycler view and connect a layout manager and the adapter
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        // Get the configuration on app creation
        getConfiguration();
    }

    // Get the list of currently playing movies from the API
    private void getNowPlaying() {
        // Create the URL
        String url = API_BASE_URL + "/movie/now_playing";
        // Set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API key, always required
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Load the result into movies list
                try {
                    JSONArray results = response.getJSONArray("results");
                    // Iterate through the result set and create Movie objects
                    for(int i = 0; i < results.length(); i++) {
                        Movie movie = new Movie(results.getJSONObject(i));
                        movies.add(movie);
                        // Notify adapter that a row was added
                        adapter.notifyItemInserted(movies.size() - 1);
                    }
                    Log.i(TAG, String.format("Loaded %s movies", results.length()));
                } catch (JSONException e) {
                    logError("Failed to parse now playing movies", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed to get data from now_playing endpoint", throwable, true);
            }
        });
    }

    // Get the configuration from the API
    private void getConfiguration() {
        // Create the URL
        String url = API_BASE_URL + "/configuration";
        // Set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, getString(R.string.api_key)); // API key, always required
        // Execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    config = new Config(response);
                    Log.i(TAG, String.format("Loaded configuration with imageBaseUrl %s and posterSize %s",
                            config.getImageBaseUrl(),
                            config.getPosterSize()));
                    // Pass config to adapter
                    adapter.setConfig(config);
                    // Get the now playing movie list
                    getNowPlaying();
                } catch (JSONException e) {
                    logError("Failed parsing configuration", e, true);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                logError("Failed getting configuration", throwable, true);
            }
        });
    }

    // Handle errors, log and alert user
    private void logError(String message, Throwable error, boolean alertUser) {
        // Always log the error
        Log.e(TAG, message, error);
        // Alert the user to avoid silent errors
        if(alertUser) {
            // Show a long toast with the error message
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }
    }
}
