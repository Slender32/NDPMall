package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.annotation.RemoveCache;
import com.slender.annotation.ServiceCache;
import com.slender.dto.product.ReviewAddRequest;
import com.slender.dto.product.ReviewUpdateRequest;
import com.slender.entity.Review;
import com.slender.enumeration.order.OrderStatus;
import com.slender.exception.order.OrderNotFoundException;
import com.slender.exception.order.OrderNotPaidSuccessException;
import com.slender.exception.product.ProductNotFoundException;
import com.slender.mapper.ReviewMapper;
import com.slender.repository.OrderRepository;
import com.slender.repository.ProductRepository;
import com.slender.repository.ReviewRepository;
import com.slender.service.interfase.ReviewService;
import com.slender.vo.ListData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl extends ServiceImpl<ReviewMapper, Review> implements ReviewService {
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    @Override
    public void comment(Long uid, Long pid, ReviewAddRequest request) {
        productRepository.findById(pid)
             .ifPresentOrElse(_ ->
                 orderRepository.get(request.getOid())
                     .ifPresentOrElse(order -> {
                          if(order.getStatus() == OrderStatus.PAID_SUCCESS)
                              this.save(new Review(pid, uid, request));
                          else throw new OrderNotPaidSuccessException();},
                     OrderNotFoundException::new),
             ProductNotFoundException::new);
    }

    @Override
    @RemoveCache
    public void delete(Long rid) {
        this.removeById(rid);
    }

    @Override
    @RemoveCache
    public void update(Long rid, ReviewUpdateRequest request) {
        reviewRepository.update(rid,request);
    }

    @Override
    @ServiceCache
    public Review get(Long rid) {
        return this.getById(rid);
    }

    @Override
    public ListData<Review> getUserList(Long uid) {
        List<Review> reviews = reviewRepository.getUserList(uid);
        return new ListData<>(reviews.size(), reviews);
    }

    @Override
    public ListData<Review> getProductList(Long pid) {
        List<Review> reviews = reviewRepository.getProductList(pid);
        return new ListData<>(reviews.size(), reviews);
    }
}
