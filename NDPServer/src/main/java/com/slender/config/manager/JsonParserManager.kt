package com.slender.config.manager

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.slender.exception.json.JsonFormatException
import com.slender.exception.json.JsonParseException
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Component

@Slf4j
@Component
@RequiredArgsConstructor
class JsonParserManager(
    private val objectMapper: ObjectMapper
) {
    fun <T> parse(json: String, clazz: Class<T>): T {
        try {
            return objectMapper.readValue(json, clazz)
        } catch (_: JsonProcessingException) {
            throw JsonParseException()
        }
    }
    fun format(data: Any): String {
        try {
            return objectMapper.writeValueAsString(data)
        } catch (_: JsonProcessingException) {
            throw JsonFormatException()
        }
    }
}
