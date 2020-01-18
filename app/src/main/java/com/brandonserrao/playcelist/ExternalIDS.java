package com.brandonserrao.playcelist;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class ExternalIDS {
    private String isrc;

    @JsonProperty("isrc")
    public String getIsrc() { return isrc; }
    @JsonProperty("isrc")
    public void setIsrc(String value) { this.isrc = value; }
}
