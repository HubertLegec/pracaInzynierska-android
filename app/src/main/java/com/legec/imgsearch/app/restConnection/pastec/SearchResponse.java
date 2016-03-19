package com.legec.imgsearch.app.restConnection.pastec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hubert on 19.03.2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"image_ids", "type"})
public class SearchResponse {
    @JsonProperty("image_ids")
    public List<Integer> imageIds = new ArrayList<>();
    @JsonProperty("type")
    public String type;
}