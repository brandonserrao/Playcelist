package com.brandonserrao.playcelist;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class CurrentTrack {
    private Context context;
    private Long timestamp;
    private Long progressMS;
    private Boolean isPlaying;
    private String currentlyPlayingType;
    private Actions actions;
    private Item item;

    @JsonProperty("context")
    public Context getContext() { return context; }
    @JsonProperty("context")
    public void setContext(Context value) { this.context = value; }

    @JsonProperty("timestamp")
    public Long getTimestamp() { return timestamp; }
    @JsonProperty("timestamp")
    public void setTimestamp(Long value) { this.timestamp = value; }

    @JsonProperty("progress_ms")
    public Long getProgressMS() { return progressMS; }
    @JsonProperty("progress_ms")
    public void setProgressMS(Long value) { this.progressMS = value; }

    @JsonProperty("is_playing")
    public Boolean getIsPlaying() { return isPlaying; }
    @JsonProperty("is_playing")
    public void setIsPlaying(Boolean value) { this.isPlaying = value; }

    @JsonProperty("currently_playing_type")
    public String getCurrentlyPlayingType() { return currentlyPlayingType; }
    @JsonProperty("currently_playing_type")
    public void setCurrentlyPlayingType(String value) { this.currentlyPlayingType = value; }

    @JsonProperty("actions")
    public Actions getActions() { return actions; }
    @JsonProperty("actions")
    public void setActions(Actions value) { this.actions = value; }

    @JsonProperty("item")
    public Item getItem() { return item; }
    @JsonProperty("item")
    public void setItem(Item value) { this.item = value; }
}
