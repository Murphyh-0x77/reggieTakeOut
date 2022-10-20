package com.liutao.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.liutao.reggie.dto.DishDto;
import com.liutao.reggie.entity.Dish;

public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品口味信息，需要操作两个表
    public void saveWithFlavor(DishDto dishDto);

    //根据id查询菜品信息和对应的口味信息
    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息，同时更新对应的口味信息
    public void updateWithFlavor(DishDto dishDto);

}
