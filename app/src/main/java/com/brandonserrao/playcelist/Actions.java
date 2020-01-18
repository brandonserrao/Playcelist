package com.brandonserrao.playcelist;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Actions {
    private Disallows disallows;

    @JsonProperty("disallows")
    public Disallows getDisallows() { return disallows; }
    @JsonProperty("disallows")
    public void setDisallows(Disallows value) { this.disallows = value; }
}
