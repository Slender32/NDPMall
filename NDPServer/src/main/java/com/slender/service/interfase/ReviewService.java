package com.slender.service.interfase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.slender.dto.product.ReviewAddRequest;
import com.slender.dto.product.ReviewUpdateRequest;
import com.slender.entity.Review;
import com.slender.vo.ListData;

public interface ReviewService extends IService<Review> {
    void comment(Long uid, Long pid, ReviewAddRequest request);

    void delete(Long rid);

    void update(Long rid, ReviewUpdateRequest request);

    ListData<Review> getProductList(Long pid);

    Review get(Long rid);

    ListData<Review> getUserList(Long uid);
}
