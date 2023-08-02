package com.tjoeun.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tjoeun.dto.ItemDTO;
import com.tjoeun.test.Student;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/test")
@Log4j2
public class Test2Controller {
	
	@GetMapping("/t1")
	public void t1(Student student, Model model) {
		model.addAttribute("text", "Spring Boot 2.7.14");
		student.setName("test1");
		student.setAge(15);
	}
	
	@GetMapping("/t2")
	public void t2(ItemDTO itemDTO) {
		itemDTO.setItemDetail("상세설명");
		itemDTO.setItemNm("상품1");
		itemDTO.setPrice(3000);
		itemDTO.setRegTime(LocalDateTime.now());
	}
	
	@GetMapping(value={"/t3", "/t4"})
	public void t3(Model model) {
		List<ItemDTO> itemList = new ArrayList<ItemDTO>();
		for(int i = 0; i < 10; i++) {
			ItemDTO itemDTO = new ItemDTO();
			itemDTO.setItemDetail("상세설명"+(i+1));
			itemDTO.setItemNm("상품"+(i+1));
			itemDTO.setPrice(3000+100*(i+1));
			itemDTO.setRegTime(LocalDateTime.now());
			
			itemList.add(itemDTO);
		}
		model.addAttribute("itemList", itemList);
	}
	
	@GetMapping(value={"/t5"})
	public void t5(String name, int height, Model model) {
		log.info(">>>>>>>>>>>> "+name+", "+height);
		model.addAttribute("name", name);
		model.addAttribute("height", height);
	}
	
	@GetMapping(value = {"/content1", "/content2"})
	public void content() {
		
	}
}
