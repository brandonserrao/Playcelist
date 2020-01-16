
package com.brandonserrao.playcelist.model;

import com.squareup.moshi.Json;

public class ExternalUrls_ {

    @Json(name = "spotify")
    private String spotify;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ExternalUrls_() {
    }

    /**
     * 
     * @param spotify
     */
    public ExternalUrls_(String spotify) {
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
