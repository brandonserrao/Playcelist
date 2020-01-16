
package com.brandonserrao.playcelist.model;

import com.squareup.moshi.Json;

public class Followers {

    @Json(name = "href")
    private Object href;
    @Json(name = "total")
    private Integer total;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Followers() {
    }

    /**
     * 
     * @param total
     * @param href
     */
    public Followers(Object href, Integer total) {
        super();
        this.href = href;
        this.total = total;
    }

    public Object getHref() {
        return href;
    }

    public void setHref(Object href) {
        this.href = href;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
