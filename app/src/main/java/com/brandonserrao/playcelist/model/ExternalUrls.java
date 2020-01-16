
package com.brandonserrao.playcelist.model;

import com.squareup.moshi.Json;

public class ExternalUrls {

    @Json(name = "spotify")
    private String spotify;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ExternalUrls() {
    }

    /**
     * 
     * @param spotify
     */
    public ExternalUrls(String spotify) {
        super();
        this.spotify = spotify;
    }

    public String getSpotify() {
        return spotify;
    }

    public void setSpotify(String spotify) {
        this.spotify = spotify;
    }

}
