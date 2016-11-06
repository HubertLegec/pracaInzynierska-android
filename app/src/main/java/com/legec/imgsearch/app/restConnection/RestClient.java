package com.legec.imgsearch.app.restConnection;

import org.androidannotations.rest.spring.annotations.Accept;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Delete;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Put;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


@Rest(rootUrl = "http://51.255.204.169", converters = { MappingJackson2HttpMessageConverter.class, StringHttpMessageConverter.class})
public interface RestClient {
    @Delete("/api/removeImage/{id}")
    @Accept(MediaType.APPLICATION_JSON)
    ResponseEntity<String> removeImage(@Path long id);

    @Get("/api/image/{id}")
    byte[] getImage(@Path long id);

    @Put("/api/uploadImage")
    ResponseEntity<InsertImageResult> insertNewImage(@Body byte[] image);

    @Put("/api/search")
    ResponseEntity<SearchResponse> search(@Body byte[] image);

    @Get("/healthCheck")
    ResponseEntity<String> healthCheck();

    void setRootUrl(String rootUrl);
    String getRootUrl();
}
