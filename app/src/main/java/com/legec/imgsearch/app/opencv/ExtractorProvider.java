package com.legec.imgsearch.app.opencv;


import com.legec.imgsearch.app.exception.InvalidExtractorName;
import com.legec.imgsearch.app.exception.NotImplementedYetException;

import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacpp.opencv_features2d.Feature2D;
import org.bytedeco.javacpp.opencv_xfeatures2d.SIFT;

/**
 * Class responsible for creating extractor objects
 */
public class ExtractorProvider {
    /**
     * Creates and returns descriptor. It's type is determined based on the given name
     * @param name descriptor name
     * @return created descriptor
     * @throws RuntimeException when name doesn't match any type of descriptor
     */
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

    /**
     * Validates given extractor name and throws the {@link InvalidExtractorName} exception when name is invalid
     * @param name extractor name to check
     * @throws InvalidExtractorName when name is invalid
     */
    private static void validateExtractorName(String name) throws InvalidExtractorName {
        if (StringUtils.isBlank(name) || name.indexOf('_') == -1 || name.endsWith("_")) {
            throw new InvalidExtractorName("Extractor name '" + name + "' is invalid");
        }
    }
}
