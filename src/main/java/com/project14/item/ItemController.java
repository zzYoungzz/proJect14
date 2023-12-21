package com.project14.item;


import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.project14.category.Category;
import com.project14.user.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.project14.cart.CartService;
import com.project14.user.SiteUser;

import lombok.RequiredArgsConstructor;
@RequestMapping("/item")
@Controller
@RequiredArgsConstructor
public class ItemController {
	private final ItemService itemService;
	private final ItemRepository itemRepository;
	private final UserService userService;
	private final CartService cartService;


	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/create")
	public String create() {

		return "item/itemForm";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/create")
	public String create(
			Item item,
			MultipartFile file

			) throws Exception {
		item.setCreateDate(LocalDateTime.now());
		item.setHit(0);
		item.setSell_count(0);

		itemService.saveItem(item,file);

		return "redirect:/";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/detail/delete/{id}")
	public String delete(@PathVariable Integer id,
			Principal principal

			) {
		Item item = this.itemService.getItem(id);
		Optional<SiteUser> user = this.userService.getByUserName(principal.getName());
		//운영자가 아닐떄
		if(!user.get().getUsername().equals("admin")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.itemService.delete(item);

		return "redirect:/";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/item/detail/delete/{id}")
	public String delete2(@PathVariable Integer id,
			Principal principal

			) {
		Item item = this.itemService.getItem(id);
		Optional<SiteUser> user = this.userService.getByUserName(principal.getName());
		//운영자가 아닐떄
		if(!user.get().getUsername().equals("admin")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
		}
		this.itemService.delete(item);

		return "redirect:/";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/detail/modify/{id}")
	public String modify(@PathVariable Integer id,Model model) {
		Item item = this.itemService.getItem(id);
		model.addAttribute("item", item);

		return "item/itemModifyForm";
	}
	//상품수정
	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("item/detail/modify/{id}")
	public String modify2(@PathVariable Integer id,Model model) {
		Item item = this.itemService.getItem(id);
		model.addAttribute("item", item);

		return "item/itemModifyForm";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/modify/{id}")
	public String modify(
			@PathVariable("id") Integer id,
			@RequestParam String name,
			@RequestParam String text,
			@RequestParam int price,
			@RequestParam int stock,
			@RequestParam int isSoldOut,
			@RequestParam Category category,
			MultipartFile file

			)throws Exception {
		Item item = this.itemService.getItem(id);
		this.itemService.modify(item, name, text, price, stock, isSoldOut, category);
		this.itemService.saveItem(item,file);

		return String.format("redirect:/item/detail/%s", id);
	}

	//item detail
	@GetMapping(value="/detail/{id}")
	public String detail(
			@PathVariable("id") int id,
			Model model
			) {

		Item item = this.itemService.getItem(id);
		this.itemService.hitAddItem(item);
		model.addAttribute("item", item);


		return "item/itemDetail";
	}

	@GetMapping(value="item/detail/{id}")
	public String detail2(
			@PathVariable("id") int id,
			Model model
			) {

		Item item = this.itemService.getItem(id);
		this.itemService.hitAddItem(item);
		model.addAttribute("item", item);
		return "item/itemDetail";
	}



	@GetMapping("/itemList")
	public String itemList(Model model) {
		List<Item> item_list = this.itemRepository.findAll(Sort.by(Sort.Direction.DESC,"createDate"));
		model.addAttribute("item_list", item_list);

		return "item/itemList";
	}


	//TODO: 카테고리별, 가격순,판매순,최신순
	@PostMapping("/search")
	public String searchItem(Model model,
			@RequestParam(value = "category_id") Integer category_id,
			@RequestParam(value = "field", defaultValue = "createDate") String field
			) {		//정렬할 필드이름 (createDate, order_cnt, price)


		List<Item> search_itemList = this.itemService.getCategoryByItemList(category_id, field);
		model.addAttribute("search_itemList", search_itemList);

		return "item/search_item";
	}


	@PostMapping("/search/all")
	public String searchItemAll(Model model,
			@RequestParam(value = "field", defaultValue = "createDate") String field
			) {		//정렬할 필드이름 (createDate, order_cnt, price)

		//기본정렬값은 최신순으로
		List<Item> search_itemList = this.itemRepository.findAll(Sort.by(Sort.Direction.DESC,field));
		model.addAttribute("search_itemList", search_itemList);
		return "item/search_item";
	}



}
