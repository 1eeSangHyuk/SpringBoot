package com.tjoeun.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.tjoeun.dto.ItemDTO;

@Controller
public class ItemController {
	
	@GetMapping("/admin/item/new")
	public String itemForm(@ModelAttribute ItemDTO itemDTO) {
		return "item/itemForm";
	}
}
