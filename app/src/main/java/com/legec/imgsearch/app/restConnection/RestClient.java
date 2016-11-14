package com.legec.imgsearch.app.restConnection;

import com.legec.imgsearch.app.restConnection.dto.ExtractorDescription;
import com.legec.imgsearch.app.restConnection.dto.Histogram;
import com.legec.imgsearch.app.restConnection.dto.MatcherDescription;
import com.legec.imgsearch.app.restConnection.dto.SearchResponse;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;

import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.Post;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.MultiValueMap;

import java.util.List;


@Rest(rootUrl = "http://51.255.204.169", converters = {MappingJackson2HttpMessageConverter.class, StringHttpMessageConverter.class, FormHttpMessageConverter.class})
public interface RestClient extends RestClientHeaders {

    @Get("/data/extractor")
    ResponseEntity<ExtractorDescription> getExtractorDescription();

    @Get("/data/matcher")
    ResponseEntity<MatcherDescription> getMatcherDescription();

    @Get("/data/vocabulary")
    ResponseEntity<Vocabulary> getVocabulary();

    @Get("/healthCheck")
    ResponseEntity<String> healthCheck();

    @Post("/findByHist")
    ResponseEntity<SearchResponse> findByHistogram(@Body List<Float> histogram);

    @Post("/findByHist/{limit}")
    ResponseEntity<SearchResponse> findByHistogramWithLimit(@Body Histogram histogram, @Path int limit);

    @Post("/find")
    @RequiresHeader("Content-Type")
    ResponseEntity<SearchResponse> find(@Body MultiValueMap<String, Object> data);

    @Post("/find/{limit}")
    @RequiresHeader("Content-Type")
    ResponseEntity<SearchResponse> findWithLimit(@Path int limit, @Body byte[] data);

    void setRootUrl(String rootUrl);
    String getRootUrl();
}
