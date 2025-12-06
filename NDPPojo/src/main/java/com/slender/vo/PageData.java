package com.slender.vo;

import java.util.List;

public record PageData<T>(
        Integer total,
        Integer totalPage,
        List<T> pageData
){}
