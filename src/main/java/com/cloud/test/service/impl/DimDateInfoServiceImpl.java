package com.cloud.test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cloud.test.entity.DimDateInfo;
import com.cloud.test.mapper.DimDateInfoMapper;
import com.cloud.test.service.IDimDateInfoService;
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
