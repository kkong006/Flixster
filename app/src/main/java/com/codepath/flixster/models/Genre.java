package com.codepath.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by kkong on 6/23/17.
 */

@Parcel // Class is parcelable
public class Genre {

    // Values from API
    private String genre;

    // Empty constructor required for Parcel
    public Genre() { }

    public Genre(JSONObject object) throws JSONException {
        genre = object.getString("genre");
    }

    public String getGenre() {
        return genre;
    }
}
