package com.github.jfcloud.jos.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.jfcloud.jos.entity.Metadata;
import com.github.jfcloud.jos.mapper.MetadataMapper;
import com.github.jfcloud.jos.service.MetadataService;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 元数据表 服务实现类
 * </p>
 *
 * @author lemon
 * @since 2021-12-28
 */
@Service
public class MetadataServiceImpl extends ServiceImpl<MetadataMapper, Metadata> implements MetadataService {

    // 根据md5码查询元数据
    @Override
    public Metadata findMetadataByMd5(String md5) {
        QueryWrapper<Metadata> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5",md5);
        Metadata metadata = this.getOne(queryWrapper);
        return metadata;
    }

    // 修改元数据表中的状态
    @Override
    public void updateStatues(List<Long> ids, Long modifiedBy, Date modifiedDate) {
        baseMapper.updateStatues(ids,modifiedBy,modifiedDate);
    }

}
