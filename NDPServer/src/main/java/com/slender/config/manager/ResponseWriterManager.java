package com.slender.config.manager;

import com.slender.result.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class ResponseWriterManager {
    private final JsonParserManager jsonParser;

    public <T> void write(Response<T> data, HttpServletResponse response){
        try {
            response.setStatus(data.code()==0?200:data.code());
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(jsonParser.format(data));
        } catch (IOException e) {
            log.error("HttpServletResponse写入失败",e);
        }
    }
}
