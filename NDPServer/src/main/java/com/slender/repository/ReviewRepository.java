package com.slender.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.slender.constant.review.ReviewColumn;
import com.slender.dto.product.ReviewUpdateRequest;
import com.slender.entity.Review;
import com.slender.exception.product.ReviewNotFoundException;
import com.slender.mapper.ReviewMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {
    private final ReviewMapper reviewMapper;

    public void update(Long rid, ReviewUpdateRequest request) {
        int update = reviewMapper.update(
                new UpdateWrapper<Review>()
                        .eq(ReviewColumn.RID, rid)
                        .set(request.getComment() != null, ReviewColumn.COMMENT, request.getComment())
                        .set(request.getStar() != null, ReviewColumn.STAR, request.getStar())
        );
        if(update==0) throw new ReviewNotFoundException();
    }

    public List<Review> getProductList(Long pid) {
        return reviewMapper.selectList(new QueryWrapper<Review>().eq(ReviewColumn.PID, pid));
    }

    public List<Review> getUserList(Long uid) {
        return reviewMapper.selectList(new QueryWrapper<Review>().eq(ReviewColumn.UID, uid));
    }
}
