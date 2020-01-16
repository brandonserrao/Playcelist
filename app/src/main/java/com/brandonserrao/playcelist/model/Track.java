
package com.brandonserrao.playcelist.model;

import java.util.List;
import com.squareup.moshi.Json;

public class Track {

    @Json(name = "album")
    private Album album;
    @Json(name = "artists")
    private List<Artist> artists = null;
    @Json(name = "available_markets")
    private List<String> availableMarkets = null;
    @Json(name = "disc_number")
    private Integer discNumber;
    @Json(name = "duration_ms")
    private Integer durationMs;
    @Json(name = "explicit")
    private Boolean explicit;
    @Json(name = "external_ids")
    private ExternalIds externalIds;
    @Json(name = "external_urls")
    private ExternalUrls_____ externalUrls;
    @Json(name = "href")
    private String href;
    @Json(name = "id")
    private String id;
    @Json(name = "name")
    private String name;
    @Json(name = "popularity")
    private Integer popularity;
    @Json(name = "preview_url")
    private String previewUrl;
    @Json(name = "track_number")
    private Integer trackNumber;
    @Json(name = "type")
    private String type;
    @Json(name = "uri")
    private String uri;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Track() {
    }

    /**
     * 
     * @param previewUrl
     * @param trackNumber
     * @param album
     * @param externalIds
     * @param externalUrls
     * @param type
     * @param uri
     * @param explicit
     * @param discNumber
     * @param artists
     * @param availableMarkets
     * @param popularity
     * @param name
     * @param href
     * @param id
     * @param durationMs
     */
    public Track(Album album, List<Artist> artists, List<String> availableMarkets, Integer discNumber, Integer durationMs, Boolean explicit, ExternalIds externalIds, ExternalUrls_____ externalUrls, String href, String id, String name, Integer popularity, String previewUrl, Integer trackNumber, String type, String uri) {
        super();
        this.album = album;
        this.artists = artists;
        this.availableMarkets = availableMarkets;
        this.discNumber = discNumber;
        this.durationMs = durationMs;
        this.explicit = explicit;
        this.externalIds = externalIds;
        this.externalUrls = externalUrls;
        this.href = href;
        this.id = id;
        this.name = name;
        this.popularity = popularity;
        this.previewUrl = previewUrl;
        this.trackNumber = trackNumber;
        this.type = type;
        this.uri = uri;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public List<String> getAvailableMarkets() {
        return availableMarkets;
    }

    public void setAvailableMarkets(List<String> availableMarkets) {
        this.availableMarkets = availableMarkets;
    }

    public Integer getDiscNumber() {
        return discNumber;
    }

    public void setDiscNumber(Integer discNumber) {
        this.discNumber = discNumber;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
    }

    public Boolean getExplicit() {
        return explicit;
    }

    public void setExplicit(Boolean explicit) {
        this.explicit = explicit;
    }

    public ExternalIds getExternalIds() {
        return externalIds;
    }

    public void setExternalIds(ExternalIds externalIds) {
        this.externalIds = externalIds;
    }

    public ExternalUrls_____ getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(ExternalUrls_____ externalUrls) {
        this.externalUrls = externalUrls;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public Integer getTrackNumber() {
        return trackNumber;
    }

    public void setTrackNumber(Integer trackNumber) {
        this.trackNumber = trackNumber;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

}
