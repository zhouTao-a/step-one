package com.cloud.mybatis.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.mybatis.entity.DimDateInfo;
import com.cloud.mybatis.mapper.DimDateInfoMapper;
import com.cloud.mybatis.service.IDimDateInfoService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 时间维度表 服务实现类
 * </p>
 *
 * @author zhout
 * @since 2022-12-16
 */
@Service
public class DimDateInfoServiceImpl extends ServiceImpl<DimDateInfoMapper, DimDateInfo> implements IDimDateInfoService {

}
