package com.liutao.reggie.dto;

import com.liutao.reggie.entity.OrderDetail;
import com.liutao.reggie.entity.Orders;
import lombok.Data;
import java.util.List;

@Data
public class OrdersDto extends Orders {

    private String userName;

    private String phone;

    private String address;

    private String consignee;

    private List<OrderDetail> orderDetails;
	
}
