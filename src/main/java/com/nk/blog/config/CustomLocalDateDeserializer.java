package com.nk.blog.config;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {
    private final DateTimeFormatter formatter;

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        try {
            String dateString = parser.getText();
            return LocalDate.parse(dateString, formatter);
        }catch (Exception ex){
            throw new IllegalArgumentException("Invalid input value for date. Expected a string field with format yyyy-MM-dd");
        }
    }
}
