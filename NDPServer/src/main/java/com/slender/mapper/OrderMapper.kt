package com.slender.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.slender.entity.Order
import org.apache.ibatis.annotations.Mapper

@Mapper
interface OrderMapper : BaseMapper<Order>
