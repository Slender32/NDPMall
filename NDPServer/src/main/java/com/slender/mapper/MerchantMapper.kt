package com.slender.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.slender.entity.Merchant
import org.apache.ibatis.annotations.Mapper

@Mapper
interface MerchantMapper : BaseMapper<Merchant>
