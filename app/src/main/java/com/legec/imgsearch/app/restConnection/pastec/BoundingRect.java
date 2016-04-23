package com.legec.imgsearch.app.restConnection.pastec;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({
        "height",
        "width",
        "x",
        "y"
})
public class BoundingRect {
    @JsonProperty("height")
    public Integer height;
    @JsonProperty("width")
    public Integer width;
    @JsonProperty("x")
    public Integer x;
    @JsonProperty("y")
    public Integer y;
}
