
package com.brandonserrao.playcelist.SPPlaylist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Owner {

    @SerializedName("external_urls")
    @Expose
    private ExternalUrls_ externalUrls;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("uri")
    @Expose
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
