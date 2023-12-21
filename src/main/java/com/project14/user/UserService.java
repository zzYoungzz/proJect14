package com.project14.user;

import java.util.Optional;

import com.project14.DataNotFoundException;
import com.project14.order.Order;
import com.project14.order.OrderRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project14.cart.Cart;
import com.project14.cart.CartRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
	private final OrderRepository orderRepository;
	private final CartRepository cartRepository;
    
    

    public SiteUser create(	   
    				       String username,
    					   String password,
    					   String email,
    					   String postcode,
    					   String address,
    					   String address_detail,
    					   String phone_number
    					   ) {
    	SiteUser user = new SiteUser();
    	user.setUsername(username);
    	user.setPassword(passwordEncoder.encode(password));
    	
    	user.setEmail(email);
    	user.setPostcode(postcode);
    	user.setAddress(address);
    	user.setAddress_detail(address_detail);
    	user.setPhone_number(phone_number);
    	user.setRole(UserRole.USER);
    	this.userRepository.save(user);
    	return user;
    }

    public Optional<SiteUser> getByUserName(String username) {
    	Optional<SiteUser> user = this.userRepository.findByusername(username);
    	if(user.isPresent()) {
    		return user;
    	}else {
    		throw new DataNotFoundException("SiteUser Not found");
    	}
    }
    

    public void deleteMember(SiteUser user) {	//order와 cart까지 모두delete
    	
    	Order order = this.orderRepository.findAllByUser(user);
    	this.orderRepository.delete(order);
    	Cart cart = this.cartRepository.findAllByUser(user);
    	this.cartRepository.delete(cart);
    	
    	if(order == null) {
    		this.userRepository.delete(user);
    	}
    	if(cart == null) {
    		this.userRepository.delete(user);
    	}
    	if(cart == null || order == null) {
    		this.userRepository.delete(user);
    	}
    	if(cart == null && order == null) {
    		this.userRepository.delete(user);
    	}
    	
    	this.userRepository.delete(user);
    }
    
}
