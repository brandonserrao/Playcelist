
package com.brandonserrao.playcelist.model;

import com.squareup.moshi.Json;

public class Owner {

    @Json(name = "external_urls")
    private ExternalUrls_ externalUrls;
    @Json(name = "href")
    private String href;
    @Json(name = "id")
    private String id;
    @Json(name = "type")
    private String type;
    @Json(name = "uri")
    private String uri;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Owner() {
    }

    /**
     * 
     * @param externalUrls
     * @param href
     * @param id
     * @param type
     * @param uri
     */
    public Owner(ExternalUrls_ externalUrls, String href, String id, String type, String uri) {
        super();
        this.externalUrls = externalUrls;
        this.href = href;
        this.id = id;
        this.type = type;
        this.uri = uri;
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
