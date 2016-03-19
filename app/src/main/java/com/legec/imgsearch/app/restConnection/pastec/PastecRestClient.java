package com.legec.imgsearch.app.restConnection.pastec;

import org.androidannotations.rest.spring.annotations.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

/**
 * Created by Hubert on 19.03.2016.
 */
@Rest(rootUrl = "http://localhost:8080",converters = {MappingJackson2HttpMessageConverter.class})
public interface PastecRestClient {
    @Put("/index/images/{imageID}")
    AddRemoveResponse insertNewImage(@Path int imageID, @Body Object image);

    @Delete("/index/images/{imageID}")
    AddRemoveResponse removeImage(@Path int imageID);

    @Post("/")
    SimpleMessage ping();

    @Post("/index/searcher")
    SearchResponse search(@Body Object image);

    @Post("/index/io")
    SimpleMessage clearIndex(@Body SimpleMessage message);


    @Post("index/io")
    SimpleMessage loadSaveIndex(@Body LoadSaveIndexMessage message);
}
