package com.liutao.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liutao.reggie.entity.DishFlavor;
import com.liutao.reggie.mapper.DishFavorMapper;
import com.liutao.reggie.service.DishFavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFavorServiceImpl extends ServiceImpl<DishFavorMapper, DishFlavor> implements DishFavorService {
}
