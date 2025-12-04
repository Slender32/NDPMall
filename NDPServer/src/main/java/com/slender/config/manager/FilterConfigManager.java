package com.slender.config.manager;

import com.slender.exception.config.URLConfigNotFoundException;
import jakarta.servlet.Filter;

import java.util.HashMap;

public final class FilterConfigManager {
    private final static HashMap<Class<? extends Filter>,String> filterConfigs=new HashMap<>();

    public FilterConfigManager addFilterConfig(Class<? extends Filter> filter, String url){
        filterConfigs.put(filter, url);
        return this;
    }

    public String getFilterURL(Class<? extends Filter> filter){
        String url = filterConfigs.getOrDefault(filter, null);
        if(url==null) throw new URLConfigNotFoundException();
        return url;
    }
}
