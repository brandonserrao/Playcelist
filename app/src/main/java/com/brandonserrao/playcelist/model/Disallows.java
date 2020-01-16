
package com.brandonserrao.playcelist.model;

import com.squareup.moshi.Json;

public class Disallows {

    @Json(name = "resuming")
    private Boolean resuming;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Disallows() {
    }

    /**
     * 
     * @param resuming
     */
    public Disallows(Boolean resuming) {
        super();
        this.resuming = resuming;
    }

    public Boolean getResuming() {
        return resuming;
    }

    public void setResuming(Boolean resuming) {
        this.resuming = resuming;
    }

}
