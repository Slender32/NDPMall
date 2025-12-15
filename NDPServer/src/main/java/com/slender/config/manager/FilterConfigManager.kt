package com.slender.config.manager

import com.slender.exception.config.URLConfigNotFoundException
import jakarta.servlet.Filter

class FilterConfigManager {
    private val filterConfigs = HashMap<Class<out Filter>, String>()
    fun addFilterConfig(filter: Class<out Filter>, url: String): FilterConfigManager {
        filterConfigs[filter] = url
        return this
    }

    fun getFilterURL(filter: Class<out Filter>): String
        = filterConfigs.getOrDefault(filter, null) ?: throw URLConfigNotFoundException()
}
