package com.project14.orderItem;

import java.time.LocalDateTime;

import com.project14.item.Item;
import com.project14.order.Order;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class OrderItem {
	

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	
	@ManyToOne
	private Order order;
	
	@ManyToOne
	private Item item;
	private int count;
	private int price;
	
	//배송지
	private String address;
	private String postcode;
	private String address_detail;
	private String shipping_name;
	
	//입금 전(1), 입금 확인(2) 기본값 =1
	private int depositStatus;


	private LocalDateTime orderDate;
}
