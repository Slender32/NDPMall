package com.slender.config.manager;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Optional;

public final class RequestConfigManager {
    public record RequestConfig(
        HttpMethod method,
        MediaType mediaType,
        boolean requireToken
    ){
        public Optional<HttpMethod> getMethod() {
            return Optional.ofNullable(method);
        }
        public Optional<MediaType> getBodyType() {
            return Optional.ofNullable(mediaType);
        }
    }

    private final static HashMap<String,RequestConfig> requestConfigs=new HashMap<>();

    public RequestConfigManager addURIConfig(String uri, HttpMethod method, MediaType mediaType,boolean requireToken){
        requestConfigs.put(uri, new RequestConfig(method, mediaType,requireToken));
        return this;
    }

    public RequestConfigManager addPOSTURIConfig(String uri, boolean requireToken){
        requestConfigs.put(uri, new RequestConfig(HttpMethod.POST, MediaType.APPLICATION_JSON,requireToken));
        return this;
    }

    public RequestConfigManager addGETURIConfig(String uri, boolean requireToken){
        requestConfigs.put(uri, new RequestConfig(HttpMethod.GET, MediaType.APPLICATION_JSON,requireToken));
        return this;
    }

    public RequestConfigManager addPUTURIConfig(String uri, boolean requireToken){
        requestConfigs.put(uri, new RequestConfig(HttpMethod.PUT, MediaType.APPLICATION_JSON,requireToken));
        return this;
    }

    public RequestConfigManager addDELETEURIConfig(String uri, boolean requireToken){
        requestConfigs.put(uri, new RequestConfig(HttpMethod.DELETE, null,requireToken));
        return this;
    }

    public Optional<RequestConfig> findRequestConfig(String uri){
        return Optional.ofNullable(requestConfigs.getOrDefault(uri,null));
    }
}
