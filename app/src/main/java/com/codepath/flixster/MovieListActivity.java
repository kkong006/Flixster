package com.codepath.flixster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MovieListActivity extends AppCompatActivity {

    // Constants

    // Base URL for the API
    public final static String API_BASE_URL = "https://api.themoviedb.org/3";
    // Parameter name for the API key
    public final static String API_KEY_PARAM = "api_key";
    // API key - TODO move to secure location
    public final static String API_KEY = "52020d11a266adbe39e923ee2044d9ca";
    // Tag for logging from this activity
    public final static String TAG = "MovieListActivity";

    // Instance fields
    AsyncHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);
        client = new AsyncHttpClient();
    }

    // Get the configuration from the API
    private void getConfiguration() {
        // Create the URL
        String url = API_BASE_URL + "/configuration";
        // Set the request parameters
        RequestParams params = new RequestParams();
        params.put(API_KEY_PARAM, API_KEY); // API key, always required
        // Execute a GET request expecting a JSON object response
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
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
