package com.legec.imgsearch.app.restConnection.pastec;

import org.androidannotations.rest.spring.annotations.*;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest(rootUrl = "http://51.255.204.169:4212",converters = {MappingJackson2HttpMessageConverter.class})
public interface PastecRestClient {
    @Put("/index/images/{imageID}")
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<AddRemoveMessage> insertNewImage(@Path int imageID, @Body Object image);

    @Delete("/index/images/{imageID}")
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<AddRemoveMessage> removeImage(@Path int imageID);

    @Post("/")
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<SimpleMessage> ping();

    @Post("/index/searcher")
    @Accept(MediaType.APPLICATION_OCTET_STREAM)
    ResponseEntity<SearchResponse> search(@Body Object image);

    @Post("/index/io")
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<SimpleMessage> clearIndex(@Body SimpleMessage message);


    @Post("index/io")
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<SimpleMessage> loadSaveIndex(@Body LoadSaveIndexMessage message);
}
