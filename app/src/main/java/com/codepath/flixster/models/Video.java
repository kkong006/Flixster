package com.codepath.flixster.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by kkong on 6/22/17.
 */

@Parcel // Class is parcelable
public class Video {

    // Values from API
    private String site;
    private String key;

    // Default constructor
    public Video() { }

    // Initialize from JSON data
    public Video(JSONObject object) throws JSONException {
        site = object.getString("site");
        key = object.getString("key");
    }

    public String getSite() {
        return site;
    }

    public String getKey() {
        return key;
    }
}
