package com.slender.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.slender.entity.Review;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper extends BaseMapper<Review> {
    List<Long> getDirtyData();
}
