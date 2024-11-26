package pl.dawid0604.pcForum.dao.post;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Converter
@RequiredArgsConstructor
class JsonPostEntityContentConverter implements AttributeConverter<List<PostEntityContent>, String> {
    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(final List<PostEntityContent> attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);

        } catch (final Exception exception) {
            throw new IllegalArgumentException("Error converting List<MyData> to JSON String", exception);
        }
    }

    @Override
    public List<PostEntityContent> convertToEntityAttribute(final String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<>() { });

        } catch (final Exception exception) {
            throw new IllegalArgumentException("Error converting JSON String to List<MyData>", exception);
        }
    }
}
