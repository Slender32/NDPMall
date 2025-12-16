package com.slender.utils

object CacheToolkit {
    fun getKey(clazz: Class<*>, id: Long) = "${clazz.simpleName}:$id"

    fun getRemoveKey(clazz: Class<*>, id: Long) = "${clazz.simpleName.removeSuffix("ServiceImpl")}:$id"
}
