package com.legec.imgsearch.app.opencv;

import com.legec.imgsearch.app.exception.NotImplementedYetException;
import com.legec.imgsearch.app.restConnection.dto.OpenCvConfig;

import org.bytedeco.javacpp.opencv_features2d.BFMatcher;
import org.bytedeco.javacpp.opencv_features2d.DescriptorMatcher;


/**
 * Class responsible for creating matcher objects.
 */
class MatcherProvider {
    /**
     * Creates and returns matcher. It's type is determined based on the given description
     * @param openCvConfig {@link OpenCvConfig} determines matcher type
     * @return created matcher
     * @throws RuntimeException when description doesn't match any known matcher
     */
    static DescriptorMatcher getMatcherByDescription(OpenCvConfig openCvConfig) throws RuntimeException {
        String name = openCvConfig.getMatcher_type();
        switch (name) {
            case "BFMatcher":
                return getBFMatcher(openCvConfig);
            default:
                throw new NotImplementedYetException("Matcher " + name + " not implemented yet");
        }
    }

    private static BFMatcher getBFMatcher(OpenCvConfig openCvConfig) {
        return new BFMatcher(openCvConfig.getNorm_type(), false);
    }
}
