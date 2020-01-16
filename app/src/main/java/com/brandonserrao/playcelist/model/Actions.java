

package com.brandonserrao.playcelist.model;

import com.squareup.moshi.Json;

public class Actions {

    @Json(name = "disallows")
    private Disallows disallows;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Actions() {
    }

    /**
     * 
     * @param disallows
     */
    public Actions(Disallows disallows) {
        super();
        this.disallows = disallows;
    }

    public Disallows getDisallows() {
        return disallows;
    }

    public void setDisallows(Disallows disallows) {
        this.disallows = disallows;
    }

}
