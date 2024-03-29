package com.legec.imgsearch.app.opencv;

import com.legec.imgsearch.app.exception.NotImplementedYetException;
import com.legec.imgsearch.app.restConnection.dto.OpenCvConfig;

import org.androidannotations.annotations.EBean;
import org.bytedeco.javacpp.opencv_features2d.BFMatcher;
import org.bytedeco.javacpp.opencv_features2d.DescriptorMatcher;
import org.bytedeco.javacpp.opencv_features2d.FlannBasedMatcher;


/**
 * Class responsible for creating matcher objects.
 */
@EBean
class MatcherProvider {
    /**
     * Creates and returns matcher. It's type is determined based on the given description
     * @param openCvConfig {@link OpenCvConfig} determines matcher type
     * @return created matcher
     * @throws RuntimeException when description doesn't match any known matcher
     */
    DescriptorMatcher getMatcherByDescription(OpenCvConfig openCvConfig) throws RuntimeException {
        String name = openCvConfig.getMatcher_type();
        switch (name) {
            case "BFMatcher":
                return getBFMatcher(openCvConfig);
            case "FlannBasedMatcher":
                return new FlannBasedMatcher();
            default:
                throw new NotImplementedYetException("Matcher " + name + " not implemented yet");
        }
    }

    private static BFMatcher getBFMatcher(OpenCvConfig openCvConfig) {
        return new BFMatcher(openCvConfig.getNorm_type(), false);
    }
}
