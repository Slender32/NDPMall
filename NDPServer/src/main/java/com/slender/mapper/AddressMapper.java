package com.slender.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.slender.entity.Address;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressMapper extends BaseMapper<Address> {
}
