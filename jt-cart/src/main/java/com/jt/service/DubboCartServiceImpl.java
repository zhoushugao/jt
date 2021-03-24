package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

@Service(timeout = 3000)
public class DubboCartServiceImpl implements DubboCartService {
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		return cartMapper.selectList(queryWrapper);
	}
	/**
	 * 更新数据信息: num    商品数量
	 * 			  update  更新时间
	 * 判断条件:where user_id and item_id 
	 * 
	 */
	@Transactional  //开启事务控制
	@Override
	public void updateCartNum(Cart cart) {
		Cart tempCart = new Cart();
		tempCart.setNum(cart.getNum()).
				 setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>();
		updateWrapper.eq("user_id", cart.getUserId())
					 .eq("item_id", cart.getItemId());
		
		cartMapper.update(tempCart, updateWrapper);
		
	}
	
	
	/**
	 * 将对象中不为null的属性当做where条件,前提是:
	 * 		保证cart中只能有2个属性不为null
	 */
	@Transactional
	@Override
	public void deleteCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = 
				    new QueryWrapper<Cart>(cart);
		cartMapper.delete(queryWrapper);
		
	}
	/**
	 * 新增添加购物车商品业务实现
	 * 1.用户第一次新增,可以直接入库
	 * 2.用户不是第一次入库,应该只做数量的修改
	 * 根据:itemId和userId查询数据库
	 */
	
	@Transactional
	@Override
	public void insertCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper =  new QueryWrapper<>();
		queryWrapper.eq("user_id", cart.getUserId())
					.eq("item_id", cart.getItemId());
		Cart cartDB=cartMapper.selectOne(queryWrapper);
			if(cartDB==null) {//用户第一次购买此商品,可以直接入库
				cart.setCreated(new Date())
					.setUpdated(cart.getCreated());
				cartMapper.insert(cart);
			}else {
				//表示用户多次把此商品加入购物车,只做数量修改
				       //原购物车此商品数量+新增此商品数量
				int num = cart.getNum()+cartDB.getNum();
				cartDB.setNum(num)
						.setUpdated(new Date());
				//入库
				cartMapper.updateById(cartDB);
				/**
				 * SQL语句: update tb_cart 
				 * 			set num=#{num},updated=#{updated},
				 * 			user_id=#{userId},item_id=#{itemId},
				 * 			.....不为空的属性全部要写
				 * 			where id = #{id}	
				 */
			}
	}
}
