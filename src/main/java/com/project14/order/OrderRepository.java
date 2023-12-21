package com.project14.order;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project14.user.SiteUser;

public interface OrderRepository extends JpaRepository<Order, Integer>{
	Order findAllByUser(SiteUser user);
	
	
}
