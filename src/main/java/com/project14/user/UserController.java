package com.project14.user;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.project14.cartItem.CartItem;
import com.project14.order.Order;
import com.project14.order.OrderService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.project14.cart.Cart;
import com.project14.cart.CartService;
import com.project14.cartItem.CartItemService;
import com.project14.item.Item;
import com.project14.item.ItemService;
import com.project14.orderItem.OrderItem;
import com.project14.orderItem.OrderItemService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/user")
public class UserController {
	
	private final UserService userService;
	private final OrderService orderService;
	private final OrderItemService orderItemService;
	private final UserRepository userRepository;
	private final CartService cartService;
	private final CartItemService cartItemService;
	private final ItemService itemService;
	

	@GetMapping("/signup")
	public String signup(UserSignupForm userSignupForm) {
		return "user/signup_form";
	}
	@PostMapping("/signup")
	public String signup(
			@Valid UserSignupForm userSignupForm,BindingResult bindingResult
			) {

		if(bindingResult.hasErrors()) {
		
			return "user/signup_form";
		}

		if(!userSignupForm.getPassword1().equals(userSignupForm.getPassword2())) {
			bindingResult.rejectValue("password2", "passwordInCorrect",
					"2개의 패스워드가 일치하지 않습니다.");

			return "user/signup_form";
		}
		

		try {
			userService.create(userSignupForm.getUsername(), 
							   userSignupForm.getPassword1(), 
							   userSignupForm.getEmail(),
							   userSignupForm.getPostcode(),
							   userSignupForm.getAddress(),
							   userSignupForm.getAddress_detail(),
							   userSignupForm.getPhone_number());


		}catch(DataIntegrityViolationException e) {		//중복시 DataIntegrityViolationException이 발생
            e.printStackTrace();
            bindingResult.reject("signupFailed", "이미 등록된 사용자이거나 이미 등록된 이메일이거나 이미 등록된 전화번호 입니다.");

            return "user/signup_form";

        }catch(Exception e) {		//그 외 해당 오류의 메시지(e.getMessage())를 출력
            e.printStackTrace();
            bindingResult.reject("signupFailed", e.getMessage());
            return "user/signup_form";
        }
		
		return "redirect:/";
	}


	@GetMapping("/signup_success")
	public String signup_success() {
		return "/user/signup_success";
	}
	

	@PreAuthorize("isAuthenticated()")
	@GetMapping("/mypage")
	public String mypage(Principal principal,
			Model model
			) {
		
		
		Optional<SiteUser> user = this.userRepository.findByusername(principal.getName());
		
		Order order = this.orderService.getOrderByUser(user.get());

		if(order == null) {
			order = this.orderService.create(user.get());
		}
			
		List<OrderItem> orderItemList = order.getOrderItemList();
		model.addAttribute("orderItemList", orderItemList);
		if(orderItemList.isEmpty()) {
			List<OrderItem> orderItemList1 = new ArrayList<>();
			model.addAttribute("orderItemList", orderItemList1);
		}
		

		int orderTotalPrice =0;
		for(OrderItem i : orderItemList) {
			orderTotalPrice += i.getPrice();
		}
		model.addAttribute("orderTotalPrice", orderTotalPrice);

		
		Cart cart = this.cartService.getBySiteUser(user.get());

		if(cart == null) {
			cart = this.cartService.create(user.get());
		}
		

		List<CartItem> cartItemList = cart.getCartItemList();

		model.addAttribute("cartItemList", cartItemList);

		if(cartItemList.isEmpty()) {
			List<CartItem> cartItemList1 = new ArrayList<>();
			model.addAttribute("cartItemList", cartItemList1);
		}
		
		
		
		
		return "user/mypage";
	}
	
	


		@GetMapping(value="item/detail/{id}")
		public String detail3(
				@PathVariable("id") int id,
				Model model
				) {
			
			Item item = this.itemService.getItem(id);
			this.itemService.hitAddItem(item);
			model.addAttribute("item", item);
			return "item/itemDetail";
		}
		


	@PreAuthorize("isAuthenticated()")
	@GetMapping(value="delete/cartItem/{id}")
	public String deleteCartItem(Principal principal,
			@PathVariable("id") int id
			) {
		Optional<SiteUser> user = this.userService.getByUserName(principal.getName());
		Cart cart = this.cartService.getBySiteUser(user.get());
		CartItem cartItem = this.cartItemService.getCartItemById(id);


		if(!cart.getUser().getUsername().equals(principal.getName())) {		//cart의 user와 로그인중인 user가 다를떄
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.cartItemService.delete(cartItem);
		
		
		return "redirect:/user/mypage";
	}



	//주문취소
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value="delete/orderItem/{id}")
	public String deleteOrderItem(Principal principal,
			@PathVariable("id") int id
			) {
		Optional<SiteUser> user = this.userService.getByUserName(principal.getName());
		Order order = this.orderService.getOrderByUser(user.get());
		OrderItem orderItem = this.orderItemService.getOrderItemById(id);


		if(!order.getUser().getUsername().equals(principal.getName())) {		//order의 user와 로그인중인 user가 다를떄
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.orderItemService.delete(orderItem);
		
		
		return "redirect:/user/mypage";
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping("mypage/delete")
	public String deleteMember(Principal principal) {
		Optional<SiteUser> user = this.userService.getByUserName(principal.getName());
		

		if(!user.get().getUsername().equals(principal.getName())) {			//로그인중인 user가 다를떄
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "탈퇴 권한이 없습니다.");
		}
		this.userService.deleteMember(user.get());
		
		return "redirect:/user/logout";
	}
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/adminPage")
	public String adminPage(Model model,Principal principal,
			@RequestParam(value="page", defaultValue="0") int page
			) {
		Optional<SiteUser> user = this.userService.getByUserName(principal.getName());


		if(!user.get().getUsername().equals("admin")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "운영자페이지에 접근할 권한이 없습니다.");
		}
		Page<OrderItem> paging = this.orderItemService.getOrderItemList(page);
		model.addAttribute("paging", paging);



		int depositBefore = this.orderItemService.getOrderItemDepositStatusSize(1);
		model.addAttribute("depositBefore", depositBefore);
		//입금전후 컬럼 개수
		int depositAfter = this.orderItemService.getOrderItemDepositStatusSize(2);
		model.addAttribute("depositAfter", depositAfter);


		return "user/adminPage";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("depositSuccess/{id}")
	public String depositSuccess(Principal principal,
			@PathVariable("id") int id
			) {
		
		Optional<SiteUser> user = this.userService.getByUserName(principal.getName());
		if(!user.get().getUsername().equals("admin")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "권한이 없습니다.");
		}
		OrderItem orderItem = this.orderItemService.getOrderItemById(id);
		this.orderItemService.depositChecked(orderItem);
		
		return "redirect:/user/adminPage";
	}
	
		

	@GetMapping("/login")
	public String login() {
		return "user/login_form";
	}
}
