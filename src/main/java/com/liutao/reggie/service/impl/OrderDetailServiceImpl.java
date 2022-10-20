package com.liutao.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.liutao.reggie.entity.OrderDetail;
import com.liutao.reggie.entity.Orders;
import com.liutao.reggie.mapper.OrderDetailMapper;
import com.liutao.reggie.mapper.OrderMapper;
import com.liutao.reggie.service.OrderDetailService;
import com.liutao.reggie.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
