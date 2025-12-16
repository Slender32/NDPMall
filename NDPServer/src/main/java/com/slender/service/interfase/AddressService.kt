package com.slender.service.interfase

import com.baomidou.mybatisplus.extension.service.IService
import com.slender.dto.user.AddressAddRequest
import com.slender.dto.user.AddressUpdateRequest
import com.slender.entity.Address
import com.slender.vo.ListData

interface AddressService : IService<Address> {
    fun getList(uid: Long, order: Boolean?): ListData<Address>

    fun add(uid: Long, request: AddressAddRequest)

    fun update(uid: Long, request: AddressUpdateRequest)

    fun delete(aid: Long)

    fun get(aid: Long): Address
}
