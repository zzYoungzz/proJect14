package com.project14.item;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.project14.category.Category;
import com.project14.category.CategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ItemService {
	private final ItemRepository itemRepository;
	private final CategoryRepository categoryRepository;
	

    public void saveItem(Item item, MultipartFile file) throws Exception {

        String orifileName = file.getOriginalFilename();
        String fileName = "";

        String projectPath = System.getProperty("user.dir") + "/src/main/resources/static/files/";


        UUID uuid = UUID.randomUUID();

        String savedFileName = uuid + "_" + orifileName; // 파일명 -> imgName

        fileName = savedFileName;

        File saveFile = new File(projectPath, fileName);

        file.transferTo(saveFile);

        item.setFilename(fileName);
        item.setFilepath("/files/" + fileName);

        itemRepository.save(item);
        
    }

    public void modify(Item item, String name, String text, int price, int stock, int isSoldOut, Category category) {
    	item.setName(name);
    	item.setText(text);
    	item.setPrice(price);
    	item.setStock(stock);
    	item.setIsSoldOut(isSoldOut);
    	item.setCategory(category);
    	this.itemRepository.save(item);
    	
    }
    
    

    public Item getItem(int id) {
    	Item item = this.itemRepository.findAllById(id);
    	return item;
    }
    

    public void hitAddItem(Item item) {
    	int hit_cnt = item.getHit();
    	hit_cnt += 1;
    	item.setHit(hit_cnt);
    	this.itemRepository.save(item);
    }
    
    
    public List<Item> getCategoryByItemList(Integer id,String field){
    	Optional<Category> category = this.categoryRepository.findById(id);
    	
    	return this.itemRepository.findAllByCategory(category.get(),Sort.by(Sort.Direction.DESC,field));
    }
    
    public void delete(Item item) {
    	this.itemRepository.delete(item);
    }

}
