
package com.brandonserrao.playcelist.model;
import java.util.List;
import com.squareup.moshi.Json;

public class Tracks {

    @Json(name = "href")
    private String href;
    @Json(name = "items")
    private List<Item> items = null;
    @Json(name = "limit")
    private Integer limit;
    @Json(name = "next")
    private String next;
    @Json(name = "offset")
    private Integer offset;
    @Json(name = "previous")
    private Object previous;
    @Json(name = "total")
    private Integer total;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Tracks() {
    }

    /**
     * 
     * @param next
     * @param total
     * @param offset
     * @param previous
     * @param limit
     * @param href
     * @param items
     */
    public Tracks(String href, List<Item> items, Integer limit, String next, Integer offset, Object previous, Integer total) {
        super();
        this.href = href;
        this.items = items;
        this.limit = limit;
        this.next = next;
        this.offset = offset;
        this.previous = previous;
        this.total = total;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Object getPrevious() {
        return previous;
    }

    public void setPrevious(Object previous) {
        this.previous = previous;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

}
