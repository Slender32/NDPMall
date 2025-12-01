package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.entity.Merchant;
import com.slender.mapper.MerchantMapper;
import com.slender.service.interfase.MerchantService;
import org.springframework.stereotype.Service;

@Service
public class MerchantServiceImpl extends ServiceImpl<MerchantMapper, Merchant> implements MerchantService {

}
