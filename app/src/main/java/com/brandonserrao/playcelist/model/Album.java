

package com.brandonserrao.playcelist.model;
import java.util.List;
import com.squareup.moshi.Json;

public class Album {

    @Json(name = "album_type")
    private String albumType;
    @Json(name = "external_urls")
    private ExternalUrls_ externalUrls;
    @Json(name = "href")
    private String href;
    @Json(name = "id")
    private String id;
    @Json(name = "images")
    private List<Image> images = null;
    @Json(name = "name")
    private String name;
    @Json(name = "type")
    private String type;
    @Json(name = "uri")
    private String uri;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Album() {
    }

    /**
     * 
     * @param images
     * @param albumType
     * @param name
     * @param externalUrls
     * @param href
     * @param id
     * @param type
     * @param uri
     */
    public Album(String albumType, ExternalUrls_ externalUrls, String href, String id, List<Image> images, String name, String type, String uri) {
        super();
        this.albumType = albumType;
        this.externalUrls = externalUrls;
        this.href = href;
        this.id = id;
        this.images = images;
        this.name = name;
        this.type = type;
        this.uri = uri;
    }

    public String getAlbumType() {
        return albumType;
    }

    public void setAlbumType(String albumType) {
        this.albumType = albumType;
    }

    public ExternalUrls_ getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(ExternalUrls_ externalUrls) {
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
