package com.slender.service.implement;

import com.slender.mapper.AddressMapper;
import com.slender.mapper.FavouriteMapper;
import com.slender.mapper.ReviewMapper;
import com.slender.service.interfase.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final AddressMapper addressMapper;
    private final FavouriteMapper favoriteMapper;
    private final ReviewMapper reviewMapper;
    @Override
    @Transactional
    public void cleanDataBase() {
        log.info("开始执行清理");

        List<Long> fids=favoriteMapper.getDirtyData();
        favoriteMapper.deleteByIds(fids);

        List<Long> aids=addressMapper.getDirtyData();
        addressMapper.deleteByIds(aids);

        List<Long> rids=reviewMapper.getDirtyData();
        reviewMapper.deleteByIds(rids);

    }
}
