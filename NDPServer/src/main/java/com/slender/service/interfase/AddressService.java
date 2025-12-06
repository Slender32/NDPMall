package com.slender.service.interfase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.slender.dto.user.AddressAddRequest;
import com.slender.dto.user.AddressUpdateRequest;
import com.slender.entity.Address;
import com.slender.vo.ListData;

public interface AddressService extends IService<Address> {
    ListData<Address> getList(Long uid, Boolean order);

    void add(Long uid, AddressAddRequest request);

    void update(Long uid, AddressUpdateRequest request);

    void delete(Long aid);

    Address get(Long aid);
}
