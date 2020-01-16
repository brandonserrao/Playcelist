
package com.brandonserrao.playcelist.model;

import java.util.List;
import com.squareup.moshi.Json;

public class User {

    @Json(name = "country")
    private String country;
    @Json(name = "display_name")
    private String displayName;
    @Json(name = "email")
    private String email;
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
    @Json(name = "product")
    private String product;
    @Json(name = "type")
    private String type;
    @Json(name = "uri")
    private String uri;

    /**
     * No args constructor for use in serialization
     * 
     */
    public User() {
    }

    /**
     * 
     * @param country
     * @param images
     * @param product
     * @param followers
     * @param displayName
     * @param externalUrls
     * @param href
     * @param id
     * @param type
     * @param uri
     * @param email
     */
    public User(String country, String displayName, String email, ExternalUrls externalUrls, Followers followers, String href, String id, List<Image> images, String product, String type, String uri) {
        super();
        this.country = country;
        this.displayName = displayName;
        this.email = email;
        this.externalUrls = externalUrls;
        this.followers = followers;
        this.href = href;
        this.id = id;
        this.images = images;
        this.product = product;
        this.type = type;
        this.uri = uri;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
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
