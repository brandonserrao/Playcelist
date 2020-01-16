
package com.brandonserrao.playcelist.model;

import com.squareup.moshi.Json;

public class Image {

    @Json(name = "height")
    private Object height;
    @Json(name = "url")
    private String url;
    @Json(name = "width")
    private Object width;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Image() {
    }

    /**
     * 
     * @param width
     * @param url
     * @param height
     */
    public Image(Object height, String url, Object width) {
        super();
        this.height = height;
        this.url = url;
        this.width = width;
    }

    public Object getHeight() {
        return height;
    }

    public void setHeight(Object height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getWidth() {
        return width;
    }

    public void setWidth(Object width) {
        this.width = width;
    }

}
