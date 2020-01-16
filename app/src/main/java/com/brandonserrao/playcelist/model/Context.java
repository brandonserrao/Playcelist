

package com.brandonserrao.playcelist.model;

import com.squareup.moshi.Json;

public class Context {

    @Json(name = "external_urls")
    private ExternalUrls externalUrls;
    @Json(name = "href")
    private String href;
    @Json(name = "type")
    private String type;
    @Json(name = "uri")
    private String uri;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Context() {
    }

    /**
     * 
     * @param externalUrls
     * @param href
     * @param type
     * @param uri
     */
    public Context(ExternalUrls externalUrls, String href, String type, String uri) {
        super();
        this.externalUrls = externalUrls;
        this.href = href;
        this.type = type;
        this.uri = uri;
    }

    public ExternalUrls getExternalUrls() {
        return externalUrls;
    }

    public void setExternalUrls(ExternalUrls externalUrls) {
        this.externalUrls = externalUrls;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
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
