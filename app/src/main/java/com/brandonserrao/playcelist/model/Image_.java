
package com.brandonserrao.playcelist.model;
import com.squareup.moshi.Json;

public class Image_ {

    @Json(name = "height")
    private Integer height;
    @Json(name = "url")
    private String url;
    @Json(name = "width")
    private Integer width;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Image_() {
    }

    /**
     * 
     * @param width
     * @param url
     * @param height
     */
    public Image_(Integer height, String url, Integer width) {
        super();
        this.height = height;
        this.url = url;
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

}
