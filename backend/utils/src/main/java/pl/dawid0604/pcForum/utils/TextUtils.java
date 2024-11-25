package pl.dawid0604.pcForum.utils;

import static org.apache.commons.lang3.StringUtils.isBlank;

public final class TextUtils {
    private TextUtils() { }

    public static int nthIndexOf(final String text, final char character, final int occurrences) {
        int index = -1;

        if(isBlank(text) || occurrences <= 0) {
            return index;
        }

        for (int i = 0; i < occurrences; i++) {
            index = text.indexOf(character, index + 1);

            if(index == -1) {
                return -1;
            }

        } return index;
    }
}
