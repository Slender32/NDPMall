package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.annotation.RemoveCache;
import com.slender.annotation.ServiceCache;
import com.slender.dto.product.CategoryAddRequest;
import com.slender.dto.product.CategoryUpdateRequest;
import com.slender.entity.Category;
import com.slender.mapper.CategoryMapper;
import com.slender.repository.CategoryRepository;
import com.slender.service.interfase.CategoryService;
import com.slender.vo.ListData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public ListData<Category> getList(Boolean order) {
        List<Category> categories = categoryRepository.getList(order);
        return new ListData<>(categories.size(), categories);
    }

    @Override
    @ServiceCache
    public Category get(Long cid) {
        return this.getById(cid);
    }

    @Override
    public void add(CategoryAddRequest request) {
        this.save(new Category(request));
    }

    @Override
    @RemoveCache
    public void delete(Long cid) {
        this.removeById(cid);
    }

    @Override
    @RemoveCache
    public void update(Long cid, CategoryUpdateRequest request) {
        categoryRepository.update(cid,request);
    }
}
