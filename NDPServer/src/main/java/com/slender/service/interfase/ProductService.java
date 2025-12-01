package com.slender.service.interfase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.slender.dto.product.ProductCommentRequest;
import com.slender.dto.product.ProductListRequest;
import com.slender.entity.Product;
import com.slender.result.Response;

import java.util.List;

public interface ProductService extends IService<Product> {
    void comment(Long uid, Long pid, ProductCommentRequest request);

    void list(Long uid, ProductListRequest request);
}
