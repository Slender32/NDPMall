package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.dto.user.FavouriteRequest;
import com.slender.entity.Favourite;
import com.slender.entity.Product;
import com.slender.mapper.FavouriteMapper;
import com.slender.service.interfase.FavouriteService;
import com.slender.vo.ListData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavouriteServiceImpl extends ServiceImpl<FavouriteMapper, Favourite> implements FavouriteService {
    private final FavouriteMapper favouriteMapper;
    @Override
    public ListData<Product> getList(Long uid) {
        List<Product> products=favouriteMapper.getList(uid);
        return new ListData<>(products.size(),products);
    }

    @Override
    public void add(Long uid, FavouriteRequest request) {
        this.save(new Favourite(uid,request));
    }

    @Override
    public void delete(Long fid) {
        this.removeById(fid);
    }
}
