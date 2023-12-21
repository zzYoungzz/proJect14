package com.project14.order;

import java.security.Principal;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.project14.item.Item;
import com.project14.item.ItemRepository;
import com.project14.item.ItemService;
import com.project14.orderItem.OrderItem;
import com.project14.orderItem.OrderItemService;
import com.project14.user.SiteUser;
import com.project14.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class OrderController {
	private final ItemService itemService;
	private final UserService userService;
	private final OrderRepository orderRepository;
	private final OrderService orderService;
	private final OrderItemService orderItemService;
	private final ItemRepository itemRepository;
	

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/order/{id}")
	public String order(
			Model model,@PathVariable(value="id") int id,
			Principal principal,OrdersForm ordersForm
			) {
		Item item = this.itemService.getItem(id);
		model.addAttribute("item", item);
		Optional<SiteUser> user = this.userService.getByUserName(principal.getName());
		model.addAttribute("user", user.get());
		
		
		return "order/orderform";
	}
	

	@PreAuthorize("isAuthenticated()")
	@PostMapping("/order/buy/{id}")
	public String buy(@Valid OrdersForm ordersForm,BindingResult bindingResult,Principal principal,
			@PathVariable(value = "id")int id) {

		if(bindingResult.hasErrors()) {
			return "order/orderform";
		}
		Item item = this.itemService.getItem(id);
		Optional<SiteUser> user = this.userService.getByUserName(principal.getName());
		
		Order order = this.orderRepository.findAllByUser(user.get());
		//order 객체가 없다면
		if(order == null) {
			order = this.orderService.create(user.get());
		}
		
		
		
		OrderItem orderItem = this.orderItemService.create(order,item, ordersForm.getCount(), 
				ordersForm.getPrice(), ordersForm.getAddress(), ordersForm.getAddress_detail(), 
				ordersForm.getPostcode(), ordersForm.getShipping_name());


		int sell_cnt = item.getStock();		//상품재고 -1
		sell_cnt -= orderItem.getCount();
		item.setStock(sell_cnt);
		int order_count = item.getSell_count();
		order_count +=1;
		item.setSell_count(order_count);
		this.itemRepository.save(item);
		
		return "redirect:/order/order_success";
	}
	

	@GetMapping("/order/order_success")
	public String order_success() {

		return "order/order_success";
	}
	
}
