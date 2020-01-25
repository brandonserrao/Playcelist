
package com.brandonserrao.playcelist.SPPlaylist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Followers {

    @SerializedName("href")
    @Expose
    private Object href;
    @SerializedName("total")
    @Expose
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
