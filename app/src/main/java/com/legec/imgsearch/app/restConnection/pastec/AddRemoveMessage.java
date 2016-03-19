package com.legec.imgsearch.app.restConnection.pastec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by Hubert on 19.03.2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"image_id", "type"})
public class AddRemoveMessage  extends SimpleMessage{
    @JsonProperty("image_id")
    public int imageId;
}