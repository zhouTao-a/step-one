package com.cloud.mybatis.controller;

import com.cloud.mybatis.entity.SysUser;
import com.cloud.mybatis.service.ISysUserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author zhouTao
 * @since 2023-02-08
 */
@RestController
@RequestMapping("/sys-user")
public class SysUserController {

    @Resource
    private ISysUserService service;

    @PostMapping("/insert")
    public boolean insertUser(@RequestBody SysUser sysUser) {
        return service.save(sysUser);
    }

    @GetMapping("/select")
    public List<SysUser> selectUser() {
        return service.list();
    }

    @GetMapping("/delete/{id}")
    public boolean deleteUserById(@PathVariable("id") Long id) {
        return service.removeById(id);
    }
}
