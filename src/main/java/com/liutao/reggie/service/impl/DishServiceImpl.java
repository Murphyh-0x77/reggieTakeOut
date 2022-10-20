package com.liutao.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liutao.reggie.dto.DishDto;
import com.liutao.reggie.entity.Dish;
import com.liutao.reggie.entity.DishFlavor;
import com.liutao.reggie.mapper.DishMapper;
import com.liutao.reggie.service.DishFavorService;
import com.liutao.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFavorService dishFavorService;

    /**
     * 新增菜品并保存口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表 dish
        this.save(dishDto);

        Long dishId = dishDto.getId();//菜品id

        List<DishFlavor> dishFlavors = dishDto.getFlavors();//菜品口味
        dishFlavors = dishFlavors.stream().map((item) -> {
           item.setDishId(dishId);
           return item;
        }).collect(Collectors.toList());

        //保存口味数据到口味表 dish_flavor
        dishFavorService.saveBatch(dishFlavors);

    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        //1、查询菜品局部信息，从 dish 表查询
        Dish byId = this.getById(id);

        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(byId, dishDto);

        //2、查询口味，从 dish_flavor
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(DishFlavor::getDishId, byId.getId());
        List<DishFlavor> list = dishFavorService.list(wrapper);

        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 更新菜品信息，同时更新对应的口味信息
     * @param dishDto
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新dish表
        this.updateById(dishDto);

        //清理当前菜品对应的口味数据--dish_flavor表的delete操作
        LambdaQueryWrapper<DishFlavor> wrapper = new LambdaQueryWrapper();
        wrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFavorService.remove(wrapper);

        //添加当前提交过来的口味数据：dish_flavor的insert操作
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dishFavorService.saveBatch(flavors);
    }
}
