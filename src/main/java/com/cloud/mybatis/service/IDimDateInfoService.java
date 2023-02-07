package com.cloud.mybatis.service;

import com.cloud.mybatis.entity.DimDateInfo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 时间维度表 服务类
 * </p>
 *
 * @author zhout
 * @since 2022-12-16
 */
public interface IDimDateInfoService extends IService<DimDateInfo> {

    /**
     * 根据日期查询 返回DimDateInfo对象
     * @param date 日期字符串
     * @return DimDateInfo
     */
    DimDateInfo getByDate(String date);
}
