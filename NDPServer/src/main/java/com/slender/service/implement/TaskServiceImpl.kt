package com.slender.service.implement

import com.slender.mapper.AddressMapper
import com.slender.mapper.FavouriteMapper
import com.slender.mapper.ReviewMapper
import com.slender.service.interfase.TaskService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
open class TaskServiceImpl(
    private val addressMapper: AddressMapper,
    private val favoriteMapper: FavouriteMapper,
    private val reviewMapper: ReviewMapper
) : TaskService {
    private val log = LoggerFactory.getLogger(javaClass)
    @Transactional
    override fun cleanDataBase() {
        log.info("开始执行清理")

        val fids = favoriteMapper.getDirtyData()
        favoriteMapper.deleteByIds(fids)

        val aids = addressMapper.getDirtyData()
        addressMapper.deleteByIds(aids)

        val rids = reviewMapper.getDirtyData()
        reviewMapper.deleteByIds(rids)
    }
}
