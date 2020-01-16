

package com.brandonserrao.playcelist.model;

import com.squareup.moshi.Json;

public class CurrentTrack {

    @Json(name = "context")
    private Context context;
    @Json(name = "timestamp")
    private Integer timestamp;
    @Json(name = "progress_ms")
    private Integer progressMs;
    @Json(name = "is_playing")
    private Boolean isPlaying;
    @Json(name = "currently_playing_type")
    private String currentlyPlayingType;
    @Json(name = "actions")
    private Actions actions;
    @Json(name = "item")
    private Item item;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CurrentTrack() {
    }

    /**
     * 
     * @param isPlaying
     * @param item
     * @param context
     * @param currentlyPlayingType
     * @param actions
     * @param timestamp
     * @param progressMs
     */
    public CurrentTrack(Context context, Integer timestamp, Integer progressMs, Boolean isPlaying, String currentlyPlayingType, Actions actions, Item item) {
        super();
        this.context = context;
        this.timestamp = timestamp;
        this.progressMs = progressMs;
        this.isPlaying = isPlaying;
        this.currentlyPlayingType = currentlyPlayingType;
        this.actions = actions;
        this.item = item;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Integer getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Integer timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getProgressMs() {
        return progressMs;
    }

    public void setProgressMs(Integer progressMs) {
        this.progressMs = progressMs;
    }

    public Boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public String getCurrentlyPlayingType() {
        return currentlyPlayingType;
    }

    public void setCurrentlyPlayingType(String currentlyPlayingType) {
        this.currentlyPlayingType = currentlyPlayingType;
    }

    public Actions getActions() {
        return actions;
    }

    public void setActions(Actions actions) {
        this.actions = actions;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
