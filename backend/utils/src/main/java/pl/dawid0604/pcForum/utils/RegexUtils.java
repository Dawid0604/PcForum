package pl.dawid0604.pcForum.utils;

import java.util.regex.Pattern;

import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.UNICODE_CASE;

public final class RegexUtils {
    private RegexUtils() { }

    public static Pattern patternFrom(final String regex) {
        return Pattern.compile(regex, CASE_INSENSITIVE | UNICODE_CASE);
    }
}
