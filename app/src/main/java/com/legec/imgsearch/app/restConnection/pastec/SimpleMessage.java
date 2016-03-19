package com.legec.imgsearch.app.restConnection.pastec;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Hubert on 19.03.2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SimpleMessage {
    @JsonProperty("type")
    public String type;
}
