package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.config.manager.UserValidatorManager;
import com.slender.dto.authentication.LogoffRequest;
import com.slender.dto.merchant.MerchantRegisterRequest;
import com.slender.dto.user.MerchantUpdateRequest;
import com.slender.entity.Merchant;
import com.slender.enumeration.authentication.CaptchaType;
import com.slender.exception.user.MerchantNotFoundException;
import com.slender.mapper.MerchantMapper;
import com.slender.repository.MerchantRepository;
import com.slender.service.interfase.MerchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {
    private final MerchantRepository merchantRepository;
    private final UserValidatorManager userValidatorManager;

    @Override
    public void register(Long uid, MerchantRegisterRequest merchantRegisterRequest) {
        String email = userValidatorManager.getLoginDataCache(uid).email();
        userValidatorManager.validateCaptcha(email, merchantRegisterRequest.getCaptcha());
        this.save(new Merchant(uid, merchantRegisterRequest));
    }

    @Override
    public void delete(Long uid, Long mid, LogoffRequest request) {
        userValidatorManager.validateCaptcha(uid, request.getCaptcha(), CaptchaType.LOGOFF);
        this.removeById(mid);
    }

    @Override
    public void update(Long mid, MerchantUpdateRequest request) {
        merchantRepository.update(mid,request);
    }

    @Override
    public Merchant getByMid(Long mid) {
        return merchantRepository.getByMid(mid)
                .orElseThrow(MerchantNotFoundException::new);
    }

    @Override
    public Merchant getByUid(Long uid) {
        return merchantRepository.getByUid(uid)
                .orElseThrow(MerchantNotFoundException::new);
    }
}
