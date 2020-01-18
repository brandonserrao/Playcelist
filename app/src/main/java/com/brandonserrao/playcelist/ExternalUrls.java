package com.brandonserrao.playcelist;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class ExternalUrls {
    private String spotify;

    @JsonProperty("spotify")
    public String getSpotify() { return spotify; }
    @JsonProperty("spotify")
    public void setSpotify(String value) { this.spotify = value; }
}
