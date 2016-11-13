package com.legec.imgsearch.app.opencv;


import com.legec.imgsearch.app.exception.NotImplementedYetException;
import com.legec.imgsearch.app.restConnection.dto.MatcherDescription;

import org.bytedeco.javacpp.opencv_features2d.BFMatcher;
import org.bytedeco.javacpp.opencv_features2d.DescriptorMatcher;


public class MatcherProvider {
    public static DescriptorMatcher getMatcherByDescription(MatcherDescription description) throws RuntimeException {
        String name = description.getMatcher().getMatcher_type();
        switch (name) {
            case "BFMatcher":
                return getBFMatcher(description);
            default:
                throw new NotImplementedYetException("Matcher " + name + " not implemented yet");
        }
    }

    private static BFMatcher getBFMatcher(MatcherDescription description) {
        return new BFMatcher(description.getMatcher().getNorm_type(), false);
    }
}
