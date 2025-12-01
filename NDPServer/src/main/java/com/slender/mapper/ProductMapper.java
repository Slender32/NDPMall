package com.slender.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.slender.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
