package com.legec.imgsearch.app.restConnection.pastec;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by Hubert on 19.03.2016.
 */
@JsonPropertyOrder({"type", "index_path"})
public class LoadSaveIndexMessage extends SimpleMessage {
    @JsonProperty("index_path")
    public String indexPath;
}
