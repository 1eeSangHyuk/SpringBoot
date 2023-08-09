package com.tjoeun.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.tjoeun.dto.ItemFormDTO;
import com.tjoeun.dto.ItemSearchDTO;
import com.tjoeun.entity.Item;
import com.tjoeun.service.ItemService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ItemController {
	
	private final ItemService itemService;
	
	@GetMapping("/admin/item/new")
	public String itemForm(@ModelAttribute ItemFormDTO itemFormDTO) {
		return "item/itemForm";
	}
	
	@PostMapping("/admin/item/new")
	public String newItem(@Valid @ModelAttribute ItemFormDTO itemFormDTO,
												BindingResult result,
												Model model,
												@RequestParam(name = "itemImgFileList") List<MultipartFile> itemImgFileList) {
		if(result.hasErrors()) {
			return "item/itemForm";
		}
		
		// 상품 이미지를 선택하지 않고 상품 저장을 누른 경우
		// 상품 이미지는 최소한 하나는 올려야 되도록 함
		if(itemImgFileList.get(0).isEmpty() && itemFormDTO.getId() == null) {
			model.addAttribute("errorMessage", "사진은 최소 하나 등록하셔야 합니다.");
			return "item/itemForm"; 
		}
		
		try {
			itemService.saveItem(itemFormDTO, itemImgFileList);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "상품 등록 중 오류가 발생했습니다.");
			return "item/itemForm"; 
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/admin/item/{itemId}")
	public String itemUpdateForm(@PathVariable Long itemId, Model model) {
		
		try {
			ItemFormDTO itemFormDTO = itemService.getItemFormDTO(itemId);
			model.addAttribute("itemFormDTO", itemFormDTO);
		} catch(Exception e) {
			model.addAttribute("errorMessage", "등록되지 않은 상품입니다.");
			model.addAttribute("itemFormDTO", new ItemFormDTO());
		}
		
		return "item/itemForm";
	}
	
	@PostMapping("/admin/item/{itemId}")
	public String itemUpdate(@PathVariable Long itemId, 
													 @Valid @ModelAttribute ItemFormDTO itemFormDTO,
													 BindingResult result, Model model,
													 @RequestParam(name = "itemImgFileList") List<MultipartFile> itemImgFileList) {
		if(result.hasErrors()) {
			return "item/itemForm";
		}
		
		// 상품 이미지를 선택하지 않고 상품 저장을 누른 경우
		// 상품 이미지는 최소한 하나는 올려야 되도록 함
		if(itemImgFileList.get(0).isEmpty() && itemFormDTO.getId() == null) {
			model.addAttribute("errorMessage", "사진은 최소 하나 등록하셔야 합니다.");
			return "item/itemForm"; 
		}
		
		try {
			itemService.updateItem(itemFormDTO, itemImgFileList);
		} catch (Exception e) {
			model.addAttribute("errorMessage", "상품 등록 중 오류가 발생했습니다.");
			return "item/itemForm";
		}
		
		return "redirect:/";
	}
	
	@GetMapping({"/admin/items", "/admin/items/{page}"})
	public String itemList(ItemSearchDTO itemSearchDTO, Model model,
												 @PathVariable Optional<Integer> page) {
		
		Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
		Page<Item> items = itemService.getAdminItemPage(itemSearchDTO, pageable);
		
		model.addAttribute("items", items);
		model.addAttribute("itemSearchDTO", itemSearchDTO);
		
		model.addAttribute("maxPage", 5);
		
		return "item/itemList";
	}
}
