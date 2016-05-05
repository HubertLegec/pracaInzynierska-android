package com.legec.imgsearch.app.restConnection;

import org.androidannotations.rest.spring.annotations.*;
import org.androidannotations.rest.spring.api.MediaType;
import org.androidannotations.rest.spring.api.RestClientHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;

@Rest(rootUrl = "http://192.168.0.7:8080",converters = MappingJackson2HttpMessageConverter.class)
public interface RestClient {
    @Delete("/api/removeImage/{id}")
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<String> removeImage(@Path int id);

    @Get("/api/image/{id}")
    byte[] getImage(@Path int id);

    @Post("/api/ping")
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<String> ping();

    @Post("/api/uploadImage")
    ResponseEntity<InsertImageResult> insertNewImage(@Body byte[] image);

    @Post("/api/search")
    ResponseEntity<SearchResponse> search(@Body byte[] image);
}
