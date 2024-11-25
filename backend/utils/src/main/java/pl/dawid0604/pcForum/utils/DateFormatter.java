package pl.dawid0604.pcForum.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import static java.time.Clock.systemDefaultZone;

public final class DateFormatter {
    private DateFormatter() { }
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String formatDate(final LocalDateTime date) {
        if (date == null) {
            return null;
        }

        LocalDateTime now = getCurrentDate();
        long tempValue;

        if ((tempValue = ChronoUnit.MINUTES.between(date, now)) < 60) {
            return (tempValue < 1) ? "Just now" : tempValue + " minutes ago";

        } else if ((tempValue = ChronoUnit.HOURS.between(date, now)) < 24) {
            return (tempValue <= 1) ? "Today" : tempValue + " hours ago";

        } else if ((tempValue = ChronoUnit.DAYS.between(date, now)) < 7) {
            return (tempValue <= 1) ? "Yesterday" : tempValue + " days ago";

        } else if ((tempValue = ChronoUnit.WEEKS.between(date, now)) < 4) {
            return (tempValue <= 1) ? "Last week" : tempValue + " weeks ago";

        } else if ((tempValue = ChronoUnit.MONTHS.between(date, now)) < 12) {
            return (tempValue <= 1) ? "Last month" : tempValue + " months ago";

        } else {
            return DATE_TIME_FORMATTER.format(date);
        }
    }

    public static LocalDateTime getCurrentDate() {
        return LocalDateTime.now(systemDefaultZone());
    }
}
