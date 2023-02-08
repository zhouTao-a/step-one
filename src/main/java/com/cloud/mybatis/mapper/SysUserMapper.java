package com.cloud.mybatis.mapper;

import com.cloud.mybatis.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author zhouTao
 * @since 2023-02-08
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

}
