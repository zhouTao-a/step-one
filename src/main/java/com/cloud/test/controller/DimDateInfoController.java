package com.cloud.test.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cloud.test.entity.DimDateInfo;
import com.cloud.test.mapper.DimDateInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 时间维度表 前端控制器
 * </p>
 *
 * @author zhout
 * @since 2022-12-16
 */
@RestController
@RequestMapping("/dim-date-info")
public class DimDateInfoController {

    private final DimDateInfoMapper mapper;

    public DimDateInfoController(DimDateInfoMapper mapper) {
        this.mapper = mapper;
    }

    @GetMapping("/list")
    public void list(){
        QueryWrapper<DimDateInfo> wrapper = new QueryWrapper<>();
        wrapper.eq("date", "2022-12-16");
        List<DimDateInfo> list = mapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

}
