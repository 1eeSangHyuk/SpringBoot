package com.tjoeun.controller;

import javax.validation.Valid;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tjoeun.dto.UsersFormDTO;
import com.tjoeun.service.UsersService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {
	
	private final UsersService usersService;
	
	@GetMapping("/signUp")
	public String signUp(@ModelAttribute UsersFormDTO usersFormDTO) {
		return "signUp_form";
	}
	
	@PostMapping("/signUp")
	public String signUp(@Valid UsersFormDTO usersFormDTO,
											 BindingResult result,
											 Model model) {
		if(result.hasErrors()) {
			return "signUp_form";
		}
		
		// 비밀번호 일치 여부 확인
		if(!usersFormDTO.getPassword1().equals(usersFormDTO.getPassword2())) {
			result.rejectValue("password2", "passwordInCorrect", "비밀번호가 일치하지 않습니다.");
			return "signUp_form";
		}
		
		// 중복 사용자 존재 여부 확인
		try{
			usersService.createUsers(usersFormDTO);
		} catch(DataIntegrityViolationException e) {
			e.printStackTrace();
			result.reject("duplicateUser", "이미 동일한 사용자명으로 가입된 사용자 또는 이메일 주소로 가입된 사용자가 존재합니다.");
			return "signUp_form";
		} catch(Exception e) {
			e.printStackTrace();
			result.reject("signUpFailed", e.getMessage());
			return "signUp_form";
		}
		return "redirect:/";
	}
	
	@GetMapping("/login")
	public String login() {
		return "login_form";
	}
}
