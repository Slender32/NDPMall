package com.slender.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.slender.entity.Favourite;
import com.slender.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FavouriteMapper extends BaseMapper<Favourite> {
    List<Product> getList(@Param("uid") Long uid);
}
