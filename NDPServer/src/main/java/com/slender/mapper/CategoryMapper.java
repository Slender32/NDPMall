package com.slender.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.slender.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
