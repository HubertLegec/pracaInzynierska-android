package com.legec.imgsearch.app.restConnection.pastec;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"type", "index_path"})
public class LoadSaveIndexMessage extends SimpleMessage {
    @JsonProperty("index_path")
    public String indexPath;
}
