package com.legec.imgsearch.app.opencv;


import com.legec.imgsearch.app.exception.NotImplementedYetException;
import com.legec.imgsearch.app.restConnection.dto.MatcherDescription;

import org.bytedeco.javacpp.opencv_features2d.BFMatcher;
import org.bytedeco.javacpp.opencv_features2d.DescriptorMatcher;

/**
 * Class responsible for creating matcher objects.
 */
class MatcherProvider {
    /**
     * Creates and returns matcher. It's type is determined based on the given description
     * @param description {@link MatcherDescription} determines matcher type
     * @return created matcher
     * @throws RuntimeException when description doesn't match any known matcher
     */
    static DescriptorMatcher getMatcherByDescription(MatcherDescription description) throws RuntimeException {
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
