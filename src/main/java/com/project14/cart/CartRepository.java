package com.project14.cart;

import java.util.List;

import com.project14.cartItem.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import com.project14.user.SiteUser;

public interface CartRepository extends JpaRepository<Cart, Integer>{
	Cart findAllByUser(SiteUser user);
	List<CartItem> findCartItemListByUser(SiteUser user);
}
