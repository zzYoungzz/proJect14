package com.project14.orderItem;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{
	Page<OrderItem> findAll(Pageable pageable);
	List<OrderItem> findAllByDepositStatus(int depositStatus);
	
	
	
}
