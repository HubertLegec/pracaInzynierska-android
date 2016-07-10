package com.legec.imgsearch.app.restConnection;

import org.androidannotations.rest.spring.annotations.*;
import org.androidannotations.rest.spring.api.MediaType;
import org.androidannotations.rest.spring.api.RestClientHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

@Rest(rootUrl = "http://192.168.0.7:8080",converters = {ByteArrayHttpMessageConverter.class, MappingJackson2HttpMessageConverter.class})
public interface RestClient extends RestClientHeaders {
    @Delete("/api/removeImage/{id}")
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<String> removeImage(@Path long id);

    @Get("/api/image/{id}")
    @RequiresHeader("Content-Type")
    byte[] getImage(@Path long id);

    @Post("/api/ping")
    @Accept(MediaType.APPLICATION_JSON)
    @RequiresHeader("Content-Type")
    ResponseEntity<String> ping();

    @Put("/api/uploadImage")
    ResponseEntity<InsertImageResult> insertNewImage(@Body byte[] image);

    @Put("/api/search")
    @RequiresHeader("Content-Type")
    ResponseEntity<SearchResponse> search(@Body byte[] image);
}
