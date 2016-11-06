package com.legec.imgsearch.app.restConnection;

import com.legec.imgsearch.app.restConnection.dto.ExtractorDescription;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Rest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


@Rest(rootUrl = "http://51.255.204.169", converters = { MappingJackson2HttpMessageConverter.class, StringHttpMessageConverter.class})
public interface RestClient {

    @Get("/data/extractor")
    ResponseEntity<ExtractorDescription> getExtractorDescription();

    @Get("/data/vocabulary")
    ResponseEntity<Vocabulary> getVocabulary();

    @Get("/healthCheck")
    ResponseEntity<String> healthCheck();

    void setRootUrl(String rootUrl);
    String getRootUrl();
}
