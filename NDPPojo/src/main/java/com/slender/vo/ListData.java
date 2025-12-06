package com.slender.vo;

import java.util.List;

public record ListData<T>(
        Integer total,
        List<T> listData
){}