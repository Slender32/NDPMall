package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.annotation.RemoveCache;
import com.slender.annotation.ServiceCache;
import com.slender.dto.product.ProductCommentRequest;
import com.slender.dto.product.ProductListRequest;
import com.slender.entity.Product;
import com.slender.exception.order.OrderNotFoundException;
import com.slender.exception.product.ProductNotFoundException;
import com.slender.mapper.ProductMapper;
import com.slender.repository.OrderRepository;
import com.slender.repository.ProductRepository;
import com.slender.repository.ReviewRepository;
import com.slender.service.interfase.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public void comment(Long uid, Long pid, ProductCommentRequest request) {
        productRepository.findById(pid)
                .ifPresentOrElse(_ ->
                        orderRepository.findById(request.getOid())
                                .ifPresentOrElse(_ -> reviewRepository.add(pid, uid, request),
                                        OrderNotFoundException::new),
                        ProductNotFoundException::new);
    }

    @Override
    @RemoveCache
    public void list(Long uid, ProductListRequest request) {
        productRepository.add(uid, request);
    }
}
