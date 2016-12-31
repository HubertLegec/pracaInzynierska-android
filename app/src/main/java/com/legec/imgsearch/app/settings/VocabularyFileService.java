package com.legec.imgsearch.app.settings;


import android.content.Context;

import com.google.gson.Gson;
import com.legec.imgsearch.app.restConnection.dto.Vocabulary;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Scanner;

@EBean
class VocabularyFileService {
    private static final String VOCABULARY_FILE_NAME = "vocabularyJSON";

    @RootContext
    Context context;

    /**
     * Convert vocabulary object to JSON and save it to file
     * @param object vocabulary object to save
     * @throws IOException when can't create or write to file
     */
    void saveVocabularyToFile(Vocabulary object) throws IOException {
        context.deleteFile(VOCABULARY_FILE_NAME);
        Gson gson = new Gson();
        String json = gson.toJson(object);
        OutputStreamWriter writer;
        writer = new OutputStreamWriter(
                context.openFileOutput(VOCABULARY_FILE_NAME, Context.MODE_PRIVATE)
        );
        writer.write(json);
        writer.close();
    }

    /**
     * Load vocabulary from file
     * @return vocabulary
     * @throws IOException when can't load from file
     */
    Vocabulary getVocabularyFromFile() throws IOException {
        Gson gson = new Gson();
        InputStream stream = context.openFileInput(VOCABULARY_FILE_NAME);
        Scanner s = new Scanner(stream).useDelimiter("\\A");
        String json = s.hasNext() ? s.next() : "";
        return gson.fromJson(json, Vocabulary.class);
    }
}
