package com.legec.imgsearch.app.restConnection.pastec;

import org.androidannotations.rest.spring.annotations.*;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by Hubert on 19.03.2016.
 */
@Rest(rootUrl = "http://localhost:4212",converters = {MappingJackson2HttpMessageConverter.class})
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
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<SearchResponse> search(@Body Object image);

    @Post("/index/io")
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<SimpleMessage> clearIndex(@Body SimpleMessage message);


    @Post("index/io")
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<SimpleMessage> loadSaveIndex(@Body LoadSaveIndexMessage message);
}
