package com.slender.repository

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper
import com.slender.constant.address.AddressColumn
import com.slender.dto.user.AddressUpdateRequest
import com.slender.entity.Address
import com.slender.mapper.AddressMapper
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AddressRepository(
    private val addressMapper: AddressMapper
) {
    fun getList(uid: Long, order: Boolean?): MutableList<Address>
        = addressMapper.selectList(
            QueryWrapper<Address>()
                .eq(AddressColumn.UID, uid)
                .orderByAsc(order != null, AddressColumn.UPDATE_TIME)
        )

    fun update(uid: Long, request: AddressUpdateRequest) {
        addressMapper.update(
            UpdateWrapper<Address>()
                .eq(AddressColumn.UID, uid)
                .set(AddressColumn.UPDATE_TIME, LocalDateTime.now())
                .set(request.province != null, AddressColumn.PROVINCE, request.province)
                .set(request.city != null, AddressColumn.CITY, request.city)
                .set(request.detail != null, AddressColumn.DETAIL, request.detail)
                .set(request.createTime != null, AddressColumn.CREATE_TIME, request.createTime)
        )
    }

    fun get(aid: Long): Address? = addressMapper.selectById(aid)
}
