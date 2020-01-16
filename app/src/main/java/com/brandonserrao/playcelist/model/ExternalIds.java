
package com.brandonserrao.playcelist.model;

import com.squareup.moshi.Json;

public class ExternalIds {

    @Json(name = "isrc")
    private String isrc;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ExternalIds() {
    }

    /**
     * 
     * @param isrc
     */
    public ExternalIds(String isrc) {
        super();
        this.isrc = isrc;
    }

    public String getIsrc() {
        return isrc;
    }

    public void setIsrc(String isrc) {
        this.isrc = isrc;
    }

}
