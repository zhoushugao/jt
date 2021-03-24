package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;
@Service
public class DubboOrderServiceImpl implements DubboOrderService {
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;


	/**
	 * 一个业务逻辑需要操作3张表数据
	 * 1.添加事务控制
	 * 2.三张表  : order orderItem  orderShipping
	 * 3.准备orderId '订单号':登录用户id+当前时间戳
	 * 4.操作3个mapper分别入库
	 */
	@Transactional
	@Override
	public String insertOrder(Order order) {
		//1.获取orderId 拼接成字符串  订单号':""+登录用户id+当前时间戳
		String orderId = ""+order.getUserId()+System.currentTimeMillis();
		//设置时间
		Date date = new Date();
		//状态Status:1.未付款 2.已付款3.未发货4.已发货5.交易成功6.交易关闭
		//入库
		order.setOrderId(orderId)
				.setStatus(1)
				.setCreated(date)
				.setUpdated(date);
		orderMapper.insert(order);
		System.out.println("入库成功!!!");
		
		//3.入库订单物流
		OrderShipping shipping = order.getOrderShipping();
		shipping.setOrderId(orderId)
				.setCreated(date)
				.setUpdated(date);
		orderShippingMapper.insert(shipping);
		System.out.println("订单入库成功!!!");
		
		//4.入库订单商品 : 1.自己遍历集合分别入库      2.自己编辑sql语句批量入库
		
		 List<OrderItem> orderLists = order.getOrderItems();
		for (OrderItem orderItem : orderLists) {
			orderItem.setOrderId(orderId)
					.setCreated(date)
					.setUpdated(date);
		}
		System.err.println("订单商品入库成功!!!!");
		return orderId;
	}


	@Override
	public Order findOrderById(String id) {
		Order order = orderMapper.selectById(id);
		OrderShipping  shipping= orderShippingMapper.selectById(id);
		QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("order_id", id);
		List<OrderItem> list=orderItemMapper.selectList(queryWrapper);
		order.setOrderItems(list).setOrderShipping(shipping);
		return order;
	}
	

}
