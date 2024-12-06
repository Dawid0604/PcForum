package pl.dawid0604.pcForum.utils.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import pl.dawid0604.pcForum.utils.constants.ActivitySummarySortType;

@Slf4j
@Component
public class ActivitySummarySortTypeConverter implements Converter<String, ActivitySummarySortType> {

    @Override
    public ActivitySummarySortType convert(@NonNull final String source) {
        try {
            return ActivitySummarySortType.valueOf(source.toUpperCase());

        } catch (IllegalArgumentException e) {
            log.error("Unknown activity summary sort type: {}", source);
            return ActivitySummarySortType.WEEK;
        }
    }
}
