package com.slender.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.slender.constant.product.ProductColumn;
import com.slender.dto.product.ProductListRequest;
import com.slender.entity.Product;
import com.slender.enumeration.product.ProductStatus;
import com.slender.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final ProductMapper productMapper;

    public Optional<Product> findById(Long pid) {
        return Optional.ofNullable(productMapper.selectOne(
                new QueryWrapper<Product>()
                        .eq(ProductColumn.PID, pid)
                        .eq(ProductColumn.STATUS, ProductStatus.LIST.getValue())
        ));
    }

    public void add(Long uid, ProductListRequest request) {
        productMapper.insertOrUpdate(new Product(uid,request));
    }
}
