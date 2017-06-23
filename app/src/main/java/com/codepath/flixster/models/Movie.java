package com.codepath.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by kkong on 6/21/17.
 */

@Parcel // Class is Parcelable
public class Movie {

    // Values from API
    private String title;
    private String overview;
    private String posterPath; // Only the path
    private String backdropPath;
    Double voteAverage;
    Integer id;

    // Default constructor required for Parceler
    public Movie() {}

    // Initialize from JSON data
    public Movie(JSONObject object) throws JSONException {
        title = object.getString("title");
        overview = object.getString("overview");
        posterPath = object.getString("poster_path");
        backdropPath = object.getString("backdrop_path");
        voteAverage = object.getDouble("vote_average");
        id = object.getInt("id");
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public Integer getId() {
        return id;
    }
}
