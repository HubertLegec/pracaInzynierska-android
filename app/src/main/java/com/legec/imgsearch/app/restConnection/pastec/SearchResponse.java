package com.legec.imgsearch.app.restConnection.pastec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"bounding_rects", "image_ids", "scores", "tags", "type"})
public class SearchResponse extends SimpleMessage {
    @JsonProperty("bounding_rects")
    public List<BoundingRect> boundingRects = new ArrayList<>();
    @JsonProperty("image_ids")
    public List<Integer> imageIds = new ArrayList<>();
    @JsonProperty("scores")
    public List<Integer> scores = new ArrayList<>();
    @JsonProperty("tags")
    public List<String> tags = new ArrayList<>();
    @JsonProperty("type")
    public String type;
}
