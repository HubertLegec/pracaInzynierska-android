package com.legec.imgsearch.app.opencv;


import com.legec.imgsearch.app.exception.InvalidExtractorName;
import com.legec.imgsearch.app.exception.NotImplementedYetException;

import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.opencv_xfeatures2d.SIFT;

public class ExtractorProvider {
    public static Feature2D getExtractorByName(String name) throws RuntimeException {
        validateExtractorName(name);
        String nameCore = name.substring(name.lastIndexOf('_') + 1);
        switch (nameCore) {
            case "SIFT":
                return new SIFT();
            default:
                throw new NotImplementedYetException("Extractor" + nameCore + " not implemented yet");
        }
    }

    private static void validateExtractorName(String name) throws InvalidExtractorName {
        if (StringUtils.isBlank(name) || name.indexOf('_') == -1 || name.endsWith("_")) {
            throw new InvalidExtractorName("Extractor name '" + name + "' is invalid");
        }
    }
}
