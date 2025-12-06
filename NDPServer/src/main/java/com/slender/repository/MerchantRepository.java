package com.slender.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.slender.constant.user.MerchantColumn;
import com.slender.dto.user.MerchantUpdateRequest;
import com.slender.entity.Merchant;
import com.slender.mapper.MerchantMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MerchantRepository {
    private final MerchantMapper merchantMapper;

    public void delete(Long uid) {
        merchantMapper.delete(
                new QueryWrapper<Merchant>()
                        .eq(MerchantColumn.UID,uid)
        );
    }

    public void update(Long mid, MerchantUpdateRequest request) {
        merchantMapper.update(
                new UpdateWrapper<Merchant>()
                        .eq(MerchantColumn.MID,mid)
                        .set(MerchantColumn.UPDATE_TIME, LocalDateTime.now())
                        .set(request.getShopName() != null, MerchantColumn.SHOP_NAME, request.getShopName())
                        .set(request.getBrandName() != null, MerchantColumn.BRAND_NAME, request.getBrandName())
                        .set(request.getCreateTime() != null, MerchantColumn.CREATE_TIME, request.getCreateTime())
        );
    }

    public Optional<Merchant> getByMid(Long mid) {
        return Optional.ofNullable(merchantMapper.selectById(mid));
    }

    public Optional<Merchant> getByUid(Long uid) {
        return Optional.ofNullable(merchantMapper.selectOne(
                new QueryWrapper<Merchant>()
                        .eq(MerchantColumn.UID,uid)
        ));
    }

}
