package com.slender.service.interfase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.slender.dto.authentication.LogoffRequest;
import com.slender.dto.merchant.MerchantRegisterRequest;
import com.slender.dto.user.MerchantUpdateRequest;
import com.slender.entity.Merchant;

public interface MerchantService extends IService<Merchant> {

    void register(Long uid, MerchantRegisterRequest merchantRegisterRequest);

    void delete(Long uid, Long mid, LogoffRequest request);

    void update(Long mid, MerchantUpdateRequest request);

    Merchant getByMid(Long mid);

    Merchant getByUid(Long uid);
}
