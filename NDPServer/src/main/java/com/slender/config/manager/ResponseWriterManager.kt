package com.slender.config.manager

import com.slender.exception.category.ResponseException
import com.slender.result.Response
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Component
import java.io.IOException

@Slf4j
@Component
@RequiredArgsConstructor
class ResponseWriterManager(
    private val jsonParser: JsonParserManager
) {
    fun <T> write(data: Response<T>, response: HttpServletResponse) {
        try {
            response.status = if (data.code == 0) 200 else data.code
            response.contentType = "application/json;charset=utf-8"
            response.writer.write(jsonParser.format(data))
        } catch (_: IOException) {
            throw ResponseException()
        }
    }
}
