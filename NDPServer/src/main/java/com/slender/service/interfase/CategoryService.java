package com.slender.service.interfase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.slender.dto.product.CategoryAddRequest;
import com.slender.dto.product.CategoryUpdateRequest;
import com.slender.entity.Category;
import com.slender.vo.ListData;

public interface CategoryService extends IService<Category> {
    ListData<Category> getList(Boolean order);

    Category get(Long cid);

    void add(CategoryAddRequest request);

    void delete(Long cid);

    void update(Long cid, CategoryUpdateRequest request);
}
