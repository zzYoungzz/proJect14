package com.project14.cart;

import java.security.Principal;

import com.project14.cartItem.CartItem;
import com.project14.cartItem.CartItemRepository;
import com.project14.cartItem.CartItemService;
import com.project14.item.Item;
import com.project14.item.ItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.project14.user.SiteUser;
import com.project14.user.UserRepository;

import lombok.RequiredArgsConstructor;

@RequestMapping("/cart")
@RequiredArgsConstructor
@Controller
public class CartController {
	private final CartRepository cartRepository;
	private final CartService cartService;
	private final UserRepository userRepository;
	private final ItemService itemService;
	private final CartItemService cartItemService;
	private final CartItemRepository cartItemRepository;


	@PreAuthorize("isAuthenticated()")
	@PostMapping("/add/{id}")
	public String addCart(
			@PathVariable(value = "id")int id,
			@RequestParam(value = "count",defaultValue = "1")int count,
			Principal principal,
			Model model
			) {
		//로그인한 사용자명으로 유저조회
		SiteUser user = this.userRepository.findAllByusername(principal.getName());
		Item item = this.itemService.getItem(id);
		Cart cart = this.cartRepository.findAllByUser(user);
		
		
		//사용자에게 카트가 없다면 사용자에게 카트생성
		if(cart == null) {
			cart = this.cartService.create(user);
		}

		int cart_id = cart.getId();
		CartItem cartItem = this.cartItemRepository.findByCartIdAndItemId(cart_id, id);
		//해당상품이 카트에 없다면 카트에 상품 추가
		if(cartItem == null) {
			cartItem = this.cartItemService.create(cart, item, count);						
		}else if(cartItem.getItem().equals(item)) {
			this.cartItemService.addCount(cartItem,count);
			return String.format("redirect:/item/detail/%s", id);
		}
		
		
		return String.format("redirect:/item/detail/%s", id);
	}

}
