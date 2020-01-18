package com.brandonserrao.playcelist;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Item {
    private Album album;
    private Album[] artists;
    private String[] availableMarkets;
    private Long discNumber;
    private Long durationMS;
    private Boolean explicit;
    private ExternalIDS externalIDS;
    private ExternalUrls externalUrls;
    private String href;
    private String id;
    private String name;
    private Long popularity;
    private String previewURL;
    private Long trackNumber;
    private String type;
    private String uri;

    @JsonProperty("album")
    public Album getAlbum() { return album; }
    @JsonProperty("album")
    public void setAlbum(Album value) { this.album = value; }

    @JsonProperty("artists")
    public Album[] getArtists() { return artists; }
    @JsonProperty("artists")
    public void setArtists(Album[] value) { this.artists = value; }

    @JsonProperty("available_markets")
    public String[] getAvailableMarkets() { return availableMarkets; }
    @JsonProperty("available_markets")
    public void setAvailableMarkets(String[] value) { this.availableMarkets = value; }

    @JsonProperty("disc_number")
    public Long getDiscNumber() { return discNumber; }
    @JsonProperty("disc_number")
    public void setDiscNumber(Long value) { this.discNumber = value; }

    @JsonProperty("duration_ms")
    public Long getDurationMS() { return durationMS; }
    @JsonProperty("duration_ms")
    public void setDurationMS(Long value) { this.durationMS = value; }

    @JsonProperty("explicit")
    public Boolean getExplicit() { return explicit; }
    @JsonProperty("explicit")
    public void setExplicit(Boolean value) { this.explicit = value; }

    @JsonProperty("external_ids")
    public ExternalIDS getExternalIDS() { return externalIDS; }
    @JsonProperty("external_ids")
    public void setExternalIDS(ExternalIDS value) { this.externalIDS = value; }

    @JsonProperty("external_urls")
    public ExternalUrls getExternalUrls() { return externalUrls; }
    @JsonProperty("external_urls")
    public void setExternalUrls(ExternalUrls value) { this.externalUrls = value; }

    @JsonProperty("href")
    public String getHref() { return href; }
    @JsonProperty("href")
    public void setHref(String value) { this.href = value; }

    @JsonProperty("id")
    public String getID() { return id; }
    @JsonProperty("id")
    public void setID(String value) { this.id = value; }

    @JsonProperty("name")
    public String getName() { return name; }
    @JsonProperty("name")
    public void setName(String value) { this.name = value; }

    @JsonProperty("popularity")
    public Long getPopularity() { return popularity; }
    @JsonProperty("popularity")
    public void setPopularity(Long value) { this.popularity = value; }

    @JsonProperty("preview_url")
    public String getPreviewURL() { return previewURL; }
    @JsonProperty("preview_url")
    public void setPreviewURL(String value) { this.previewURL = value; }

    @JsonProperty("track_number")
    public Long getTrackNumber() { return trackNumber; }
    @JsonProperty("track_number")
    public void setTrackNumber(Long value) { this.trackNumber = value; }

    @JsonProperty("type")
    public String getType() { return type; }
    @JsonProperty("type")
    public void setType(String value) { this.type = value; }

    @JsonProperty("uri")
    public String getURI() { return uri; }
    @JsonProperty("uri")
    public void setURI(String value) { this.uri = value; }
}
