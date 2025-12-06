package com.slender.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.slender.constant.product.CategoryColumn;
import com.slender.dto.product.CategoryUpdateRequest;
import com.slender.entity.Category;
import com.slender.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CategoryRepository {
    private final CategoryMapper categoryMapper;

    public List<Category> getList(Boolean order) {
        return categoryMapper.selectList(
                new QueryWrapper<Category>()
                .orderByDesc(order, CategoryColumn.CREATE_TIME)
        );
    }

    public void update(Long cid, CategoryUpdateRequest request) {
        categoryMapper.update(
                new UpdateWrapper<Category>()
                        .eq(CategoryColumn.CID, cid)
                        .set(CategoryColumn.UPDATE_TIME, LocalDateTime.now())
                        .set(request.getKindName() != null, CategoryColumn.KIND_NAME, request.getKindName())
                        .set(request.getCreateTime() != null, CategoryColumn.CREATE_TIME, request.getCreateTime())
        );
    }
}
