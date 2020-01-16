
package com.brandonserrao.playcelist.model;

import java.util.List;
import com.squareup.moshi.Json;

public class Playlist {

    @Json(name = "collaborative")
    private Boolean collaborative;
    @Json(name = "description")
    private String description;
    @Json(name = "external_urls")
    private ExternalUrls externalUrls;
    @Json(name = "followers")
    private Followers followers;
    @Json(name = "href")
    private String href;
    @Json(name = "id")
    private String id;
    @Json(name = "images")
    private List<Image> images = null;
    @Json(name = "name")
    private String name;
    @Json(name = "owner")
    private Owner owner;
    @Json(name = "public")
    private Object _public;
    @Json(name = "snapshot_id")
    private String snapshotId;
    @Json(name = "tracks")
    private Tracks tracks;
    @Json(name = "type")
    private String type;
    @Json(name = "uri")
    private String uri;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Playlist() {
    }

    /**
     * 
     * @param owner
     * @param images
     * @param snapshotId
     * @param _public
     * @param collaborative
     * @param description
     * @param externalUrls
     * @param type
     * @param uri
     * @param tracks
     * @param followers
     * @param name
     * @param href
     * @param id
     */
    public Playlist(Boolean collaborative, String description, ExternalUrls externalUrls, Followers followers, String href, String id, List<Image> images, String name, Owner owner, Object _public, String snapshotId, Tracks tracks, String type, String uri) {
        super();
        this.collaborative = collaborative;
        this.description = description;
        this.externalUrls = externalUrls;
        this.followers = followers;
        this.href = href;
        this.id = id;
        this.images = images;
        this.name = name;
        this.owner = owner;
        this._public = _public;
        this.snapshotId = snapshotId;
        this.tracks = tracks;
        this.type = type;
        this.uri = uri;
    }

    public Boolean getCollaborative() {
        return collaborative;
    }

    public void setCollaborative(Boolean collaborative) {
        this.collaborative = collaborative;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(Followers followers) {
        this.followers = followers;
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

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Object getPublic() {
        return _public;
    }

    public void setPublic(Object _public) {
        this._public = _public;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
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
