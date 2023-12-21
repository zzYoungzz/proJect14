package com.project14;



import com.project14.category.Category;
import com.project14.category.CategoryRepository;
import com.project14.order.OrderRepository;
import com.project14.order.OrderService;

import com.project14.orderItem.OrderItemService;
import com.project14.user.UserRole;
import com.project14.user.UserService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;


import com.project14.cart.CartRepository;
import com.project14.item.ItemRepository;
import com.project14.item.ItemService;
import com.project14.user.SiteUser;
import com.project14.user.UserRepository;

@SpringBootTest
class project14ApplicationTests {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private CategoryRepository categoryRepository;
	@Autowired
	private ItemRepository itemRepository;
	@Autowired
	private ItemService itemService;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderItemService orderItemService;


	@Test
	@Disabled
	void adminCreateTest() {
		SiteUser admin = new SiteUser();
		admin.setUsername("admin");
		String password = "1234";
		admin.setPassword(passwordEncoder.encode(password));
		admin.setPostcode("00000");
		admin.setAddress("home");
		admin.setAddress_detail("apartment");
		admin.setEmail("buzz123@gmail.com");
		admin.setPhone_number("010-0000-0000");
		admin.setRole(UserRole.ADMIN);
		this.userRepository.save(admin);

	}
	

	@Test
	@Disabled
	void create_Category() {
		Category top = new Category();
		top.setCategory_name("상의");
		Category pants = new Category();
		pants.setCategory_name("하의");
		Category author = new Category();
		author.setCategory_name("아우터");
		this.categoryRepository.save(top);
		this.categoryRepository.save(pants);
		this.categoryRepository.save(author);

	}

}
