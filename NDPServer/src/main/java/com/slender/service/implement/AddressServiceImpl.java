package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.dto.user.AddressAddRequest;
import com.slender.dto.user.AddressUpdateRequest;
import com.slender.entity.Address;
import com.slender.exception.user.AddressNotFoundException;
import com.slender.mapper.AddressMapper;
import com.slender.repository.AddressRepository;
import com.slender.service.interfase.AddressService;
import com.slender.vo.ListData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {
    private final AddressRepository addressRepository;
    @Override
    public ListData<Address> getList(Long uid, Boolean order) {
        List<Address> list = addressRepository.getList(uid, order);
        return new ListData<>(list.size(),list);
    }

    @Override
    public void add(Long uid, AddressAddRequest request) {
        this.save(new Address(uid,request));
    }

    @Override
    public void update(Long uid, AddressUpdateRequest request) {
        addressRepository.update(uid,request);
    }

    @Override
    public void delete(Long aid) {
        this.removeById(aid);
    }

    @Override
    public Address get(Long aid) {
        return addressRepository.get(aid)
                .orElseThrow(AddressNotFoundException::new);
    }
}
