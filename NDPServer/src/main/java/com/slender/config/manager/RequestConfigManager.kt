package com.slender.config.manager

import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.util.*

class RequestConfigManager {

    data class RequestConfig(
        val method: HttpMethod? = null,
        val mediaType: MediaType? = null,
        val requireToken: Boolean
    ) {
        fun getMethod(): Optional<HttpMethod> = Optional.ofNullable(method)
        fun getBodyType(): Optional<MediaType> = Optional.ofNullable(mediaType)
    }

    companion object {
        private val requestConfigs = HashMap<String, RequestConfig>()
    }

    fun addURIConfig(uri: String, method: HttpMethod?, mediaType: MediaType?, requireToken: Boolean): RequestConfigManager {
        requestConfigs[uri] = RequestConfig(method, mediaType, requireToken)
        return this
    }

    fun addPOSTURIConfig(uri: String, requireToken: Boolean): RequestConfigManager {
        requestConfigs[uri] = RequestConfig(HttpMethod.POST, MediaType.APPLICATION_JSON, requireToken)
        return this
    }

    fun addGETURIConfig(uri: String, requireToken: Boolean): RequestConfigManager {
        requestConfigs[uri] = RequestConfig(HttpMethod.GET, null, requireToken)
        return this
    }

    fun addPUTURIConfig(uri: String, requireToken: Boolean): RequestConfigManager {
        requestConfigs[uri] = RequestConfig(HttpMethod.PUT, MediaType.APPLICATION_JSON, requireToken)
        return this
    }

    fun addDELETEURIConfig(uri: String, requireToken: Boolean): RequestConfigManager {
        requestConfigs[uri] = RequestConfig(HttpMethod.DELETE, null, requireToken)
        return this
    }

    fun findRequestConfig(uri: String): Optional<RequestConfig> {
        return Optional.ofNullable(requestConfigs[uri])
    }
}