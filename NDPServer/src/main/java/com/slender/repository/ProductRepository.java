package com.slender.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.slender.constant.product.ProductColumn;
import com.slender.constant.product.ProductConstant;
import com.slender.dto.product.ProductPageRequest;
import com.slender.dto.product.ProductUpdateRequest;
import com.slender.entity.Product;
import com.slender.enumeration.product.ProductStatus;
import com.slender.mapper.ProductMapper;
import com.slender.vo.PageData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductRepository {
    private final ProductMapper productMapper;

    public Optional<Product> findById(Long pid) {
        return Optional.ofNullable(productMapper.selectOne(
                new QueryWrapper<Product>()
                        .eq(ProductColumn.PID, pid)
                        .eq(ProductColumn.STATUS, ProductStatus.LIST)
        ));
    }

    public PageData<Product> get(ProductPageRequest request) {
        Page<Product> page = Page.of(request.getPage(), request.getPageSize());
        page.addOrder(OrderItem.withExpression(ProductColumn.UPDATE_TIME, request.getOrder()));

        QueryWrapper<Product> condition = new QueryWrapper<Product>()
                .eq(request.getMid() != null, ProductColumn.MID, request.getMid())
                .eq(request.getCid() != null, ProductColumn.CID, request.getCid())
                .eq(ProductColumn.STATUS, ProductStatus.LIST)
                .like(request.getProductName() != null, ProductColumn.PRODUCT_NAME, request.getProductName());

        if(request.getCreateTimeStart() != null && request.getCreateTimeEnd() != null){
            condition.between(ProductColumn.CREATE_TIME, request.getCreateTimeStart(), request.getCreateTimeEnd());
        }else if(request.getCreateTimeStart() != null){
            condition.ge(ProductColumn.CREATE_TIME, request.getCreateTimeStart());
        }else if(request.getCreateTimeEnd() != null){
            condition.le(ProductColumn.CREATE_TIME, request.getCreateTimeEnd());
        }

        if (request.getRemainStart() != null && request.getRemainEnd() != null) {
            condition.between(ProductColumn.REMAIN, request.getRemainStart(), request.getRemainEnd());
        } else if (request.getRemainStart() != null) {
            condition.ge(ProductColumn.REMAIN, request.getRemainStart());
        } else if (request.getRemainEnd() != null) {
            condition.le(ProductColumn.REMAIN, request.getRemainEnd());
        }

        if (request.getPriceStart() != null && request.getPriceEnd() != null) {
            condition.between(ProductColumn.PRICE, request.getPriceStart(), request.getPriceEnd());
        } else if (request.getPriceStart() != null) {
            condition.ge(ProductColumn.PRICE, request.getPriceStart());
        } else if (request.getPriceEnd() != null) {
            condition.le(ProductColumn.PRICE, request.getPriceEnd());
        }

        if (request.getSaleAmountStart() != null && request.getSaleAmountEnd() != null) {
            condition.between(ProductColumn.SALE_AMOUNT, request.getSaleAmountStart(), request.getSaleAmountEnd());
        } else if (request.getSaleAmountStart() != null) {
            condition.ge(ProductColumn.SALE_AMOUNT, request.getSaleAmountStart());
        } else if (request.getSaleAmountEnd() != null) {
            condition.le(ProductColumn.SALE_AMOUNT, request.getSaleAmountEnd());
        }

        Page<Product> data = productMapper.selectPage(page, condition);
        return new PageData<>((int) data.getTotal(), (int) data.getPages(), data.getRecords());
    }

    public void update(Long pid,ProductUpdateRequest request) {
        UpdateWrapper<Product> condition = new UpdateWrapper<>();
        if (request.getCid() == null) condition.set(ProductColumn.CID, ProductConstant.DEFAULT_CATEGORY);
        else condition.set(ProductColumn.CID,request.getCid());
        condition
            .eq(ProductColumn.PID, pid)
            .set(ProductColumn.UPDATE_TIME, LocalDateTime.now())
            .set(request.getProductName() != null, ProductColumn.PRODUCT_NAME, request.getProductName())
            .set(request.getRemain() != null, ProductColumn.REMAIN, request.getRemain())
            .set(request.getPrice() != null, ProductColumn.PRICE, request.getPrice())
            .set(request.getDescription() != null, ProductColumn.DESCRIPTION, request.getDescription())
            .set(request.getStatus() != null, ProductColumn.STATUS, request.getStatus());
        productMapper.update(condition);

    }

    public void updateRemain(Long pid, int quantity) {
        productMapper.updateRemain(new QueryWrapper<Product>().eq(ProductColumn.PID, pid), quantity);
    }
}
