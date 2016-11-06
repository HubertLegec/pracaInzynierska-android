package com.legec.imgsearch.app.utils;


import android.content.Context;

import com.google.gson.Gson;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.io.IOException;
import java.io.OutputStreamWriter;

@EBean
public class FileUtils {
    public static final String VOCABULARY_FILE_NAME = "vocabularyJSON";

    @RootContext
    Context context;

    /**
     * Converts object to JSON and saves it as string to file
     * @param object object to save
     * @param fileName name of file to create, if file with such name exist, it will be overwritten
     * @throws IOException when can't create or write to file
     */
    public void saveObjectToFile(Object object, String fileName) throws IOException {
        context.deleteFile(fileName);
        Gson gson = new Gson();
        String json = gson.toJson(object);
        OutputStreamWriter writer;
        writer = new OutputStreamWriter(context.openFileOutput(fileName, Context.MODE_PRIVATE));
        writer.write(json);
        writer.close();
    }
}
