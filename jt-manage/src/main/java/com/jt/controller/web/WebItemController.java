package com.jt.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.ItemService;

//后台接收用户请求获取商品信息
//url:manage.jt.com/web/item/findItemById?id=562379
@RestController
@RequestMapping("/web/item")
public class WebItemController {
	
	@Autowired
	private ItemService itemService;
	
	//查询item对象
	@RequestMapping("/findItemById")
	public Item findItemById(Long id) {
		
		return itemService.findItemById(id);
	}
	//http://manage.jt.com/web/item/findItemById
		//?id=562379
	//查询itemDesc对象
	@RequestMapping("/findItemDescById")
	public ItemDesc findItemDescById(Long id) {
		
		return itemService.findItemDescById(id);
	}
}
