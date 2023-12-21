package com.project14.item;

import java.util.List;

import com.project14.category.Category;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Integer>{
	Item findAllById(int id);
	List<Item> findAllByCategory(Category category, Sort sort);
	List<Item> findAll(Sort sort);
}
