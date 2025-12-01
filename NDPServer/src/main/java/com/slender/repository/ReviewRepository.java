package com.slender.repository;

import com.slender.dto.product.ProductCommentRequest;
import com.slender.entity.Review;
import com.slender.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {
    private final ReviewMapper reviewMapper;

    public void add(Long pid, Long uid, ProductCommentRequest request) {
        reviewMapper.insertOrUpdate(new Review(pid, uid, request));
    }
}
