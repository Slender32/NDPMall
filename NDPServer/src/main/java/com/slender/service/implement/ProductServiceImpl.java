package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.annotation.RemoveCache;
import com.slender.annotation.ServiceCache;
import com.slender.dto.product.ProductAddRequest;
import com.slender.dto.product.ProductUpdateRequest;
import com.slender.dto.product.ProductPageRequest;
import com.slender.entity.Product;
import com.slender.exception.product.ProductNotFoundException;
import com.slender.exception.user.MerchantNotFoundException;
import com.slender.mapper.ProductMapper;
import com.slender.repository.MerchantRepository;
import com.slender.repository.ProductRepository;
import com.slender.service.interfase.ProductService;
import com.slender.vo.PageData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    private final ProductRepository productRepository;
    private final MerchantRepository merchantRepository;

    @Override
    @ServiceCache
    public Product get(Long pid) {
        return productRepository.findById(pid)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public PageData<Product> get(ProductPageRequest request) {
        return productRepository.get(request);
    }

    @Override
    public void add(Long uid, ProductAddRequest request) {
        merchantRepository.getByUid(uid)
                .ifPresentOrElse(merchant -> this.save(new Product(merchant.getMid(), request)),
                        MerchantNotFoundException::new);
    }

    @Override
    @RemoveCache
    public void update(Long pid, ProductUpdateRequest request) {
        productRepository.update(pid,request);
    }

    @Override
    @RemoveCache
    public void delete(Long pid) {
        this.removeById(pid);
    }
}
