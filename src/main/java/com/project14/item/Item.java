package com.project14.item;

import java.time.LocalDateTime;

import com.project14.category.Category;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int id;
	
	private String name;
	
	@Column(columnDefinition = "TEXT")
	private String text;
	
	
	private int price;
	private int stock;
	
	private int isSoldOut;		//판매여부 (0 = 판매 / 1 = 판매중)
	
	@Column(length = 150)
	private String filename;
	
	@Column(length = 300)
	private String filepath;
	
	private LocalDateTime createDate;
	
	private int hit;		//조회수
	
	private int sell_count; //판매량
	
	@ManyToOne
	private Category category;
}
