
package com.brandonserrao.playcelist.model;

import com.squareup.moshi.Json;

public class ExternalUrls_____ {

    @Json(name = "spotify")
    private String spotify;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ExternalUrls_____() {
    }

    /**
     * 
     * @param spotify
     */
    public ExternalUrls_____(String spotify) {
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
