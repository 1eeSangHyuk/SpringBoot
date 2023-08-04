package com.tjoeun.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tjoeun.dto.MemberFormDTO;
import com.tjoeun.entity.Member;
import com.tjoeun.service.MemberService;

import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/member")
@Log4j2
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@GetMapping("/new")
	public String getMemberForm(MemberFormDTO memberFormDTO) {
		return "member/memberForm";
	}
	
	@PostMapping("/new")
	public String postMemberForm(@Valid MemberFormDTO memberFormDTO,
								 BindingResult result, Model model) {
		if(result.hasErrors()) {
			return "member/memberForm";
		}
		
		try {
			Member member = Member.createMember(memberFormDTO, passwordEncoder);
			Member newMember = memberService.saveMember(member);
			if(newMember != null) {
				log.info(">>>>>>>>>> 회원가입 완료 : "+newMember);
			} else {
				log.info(">>>>>>>>>> 회원가입 실패");
			}
		} catch (Exception e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "member/memberForm";
		}
		
		return "redirect:/";
	}
	
	@GetMapping("/login")
	public String login() {
		return "member/memberLoginForm";
	}
	
//	@PostMapping("/login")
//	public String login(Model model) {
//		return "";
//	}
	
	@GetMapping("/login/error")
	public String loginError(Model model) {
		model.addAttribute("loginErrorMessage", "이메일 또는 비밀번호를 확인해주세요.");
		return "member/memberLoginForm";
	}
	
	@GetMapping("/logout")
	public String logout() {
		return "redirect:/";
	}
}
