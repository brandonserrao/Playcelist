package com.brandonserrao.playcelist;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class Context {
    private ExternalUrls externalUrls;
    private String href;
    private String type;
    private String uri;

    @JsonProperty("external_urls")
    public ExternalUrls getExternalUrls() { return externalUrls; }
    @JsonProperty("external_urls")
    public void setExternalUrls(ExternalUrls value) { this.externalUrls = value; }

    @JsonProperty("href")
    public String getHref() { return href; }
    @JsonProperty("href")
    public void setHref(String value) { this.href = value; }

    @JsonProperty("type")
    public String getType() { return type; }
    @JsonProperty("type")
    public void setType(String value) { this.type = value; }

    @JsonProperty("uri")
    public String getURI() { return uri; }
    @JsonProperty("uri")
    public void setURI(String value) { this.uri = value; }
}
