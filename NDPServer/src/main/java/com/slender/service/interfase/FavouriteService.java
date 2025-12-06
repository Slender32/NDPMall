package com.slender.service.interfase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.slender.dto.user.FavouriteRequest;
import com.slender.entity.Favourite;
import com.slender.entity.Product;
import com.slender.vo.ListData;

public interface FavouriteService extends IService<Favourite> {
    ListData<Product> getList(Long uid);

    void add(Long uid, FavouriteRequest request);

    void delete(Long fid);
}
