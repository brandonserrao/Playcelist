package com.brandonserrao.playcelist;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Disallows {
    private Boolean resuming;

    @JsonProperty("resuming")
    public Boolean getResuming() { return resuming; }
    @JsonProperty("resuming")
    public void setResuming(Boolean value) { this.resuming = value; }
}
