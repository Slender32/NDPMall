package com.slender.service.implement

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.slender.dto.user.AddressAddRequest
import com.slender.dto.user.AddressUpdateRequest
import com.slender.entity.Address
import com.slender.exception.user.AddressNotFoundException
import com.slender.mapper.AddressMapper
import com.slender.repository.AddressRepository
import com.slender.service.interfase.AddressService
import com.slender.vo.ListData
import org.springframework.stereotype.Service

@Service
open class AddressServiceImpl(
    private val addressRepository: AddressRepository
) : ServiceImpl<AddressMapper, Address>(), AddressService {
    override fun getList(uid: Long, order: Boolean?): ListData<Address>
        = addressRepository.getList(uid, order)
            .let { ListData(it.size, it) }

    override fun add(uid: Long, request: AddressAddRequest) {
        save(Address(uid, request))
    }

    override fun update(uid: Long, request: AddressUpdateRequest) = addressRepository.update(uid, request)

    override fun delete(aid: Long) {
        removeById(aid)
    }
    override fun get(aid: Long): Address = addressRepository.get(aid) ?: throw AddressNotFoundException()
}