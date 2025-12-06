package com.slender.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.slender.constant.address.AddressColumn;
import com.slender.dto.user.AddressUpdateRequest;
import com.slender.entity.Address;
import com.slender.mapper.AddressMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AddressRepository {
    private final AddressMapper addressMapper;

    public List<Address> getList(Long uid, Boolean order) {
        return addressMapper.selectList(
                new QueryWrapper<Address>()
                        .eq(AddressColumn.UID, uid)
                        .orderByAsc(order!=null,AddressColumn.UPDATE_TIME));
    }

    public void update(Long uid, AddressUpdateRequest request) {
        addressMapper.update(new UpdateWrapper<Address>()
                .eq(AddressColumn.UID,uid)
                .set(AddressColumn.UPDATE_TIME, LocalDateTime.now())
                .set(request.getProvince() != null, AddressColumn.PROVINCE,request.getProvince())
                .set(request.getCity() != null, AddressColumn.CITY, request.getCity())
                .set(request.getDetail() != null, AddressColumn.DETAIL, request.getDetail())
                .set(request.getCreateTime() != null, AddressColumn.CREATE_TIME, request.getCreateTime())
        );
    }

    public Optional<Address> get(Long aid) {
        return Optional.ofNullable(addressMapper.selectById(aid));
    }
}
