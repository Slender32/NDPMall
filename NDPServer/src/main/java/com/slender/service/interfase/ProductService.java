package com.slender.service.interfase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.slender.dto.product.ProductAddRequest;
import com.slender.dto.product.ProductUpdateRequest;
import com.slender.dto.product.ProductPageRequest;
import com.slender.entity.Product;
import com.slender.vo.FileData;
import com.slender.vo.PageData;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService extends IService<Product> {
    Product get(Long pid);

    PageData<Product> get(ProductPageRequest request);

    void add(Long uid, ProductAddRequest request);

    void update(Long pid, ProductUpdateRequest request);

    void delete(Long pid);

    FileData updateImage(Long pid, MultipartFile file);

    void export(HttpServletResponse response);
}
