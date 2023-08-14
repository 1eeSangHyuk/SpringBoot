package com.tjoeun.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.tjoeun.dto.AnswerFormDTO;
import com.tjoeun.dto.QuestionFormDTO;
import com.tjoeun.entity.Question;
import com.tjoeun.entity.Users;
import com.tjoeun.service.QuestionService;
import com.tjoeun.service.UsersService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
	
	private final QuestionService questionService;
	private final UsersService usersService;
	
	// 검색 -  @RequestParam(value = "keyword", defaultValue = "") String keyword 추가
	@GetMapping("/list")
	public String list(Model model, 
			 @RequestParam(value = "page", defaultValue = "0") int page,
			 @RequestParam(value = "keyword", defaultValue = "") String keyword) {

	Page<Question> paging = questionService.findAll(page, keyword);
	model.addAttribute("paging", paging);
	model.addAttribute("keyword", keyword);
	
	return "question_list";
	}
	
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Long id, Model model,
											 @ModelAttribute AnswerFormDTO answerFormDTO) {
		
		Question question = questionService.findById(id);
		model.addAttribute("question", question);
		
		return "question_detail";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String create(@ModelAttribute QuestionFormDTO questionFormDTO) {
		return "question_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String create(@Valid QuestionFormDTO questionFormDTO,
												BindingResult result,
												Principal principal) {
		if(result.hasErrors()) {
			return "question_form";
		}
		Users user = usersService.getUsers(principal.getName());
		questionService.save(questionFormDTO.getSubject() ,questionFormDTO.getContent(), user);
		return "redirect:/question/list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String modify(@ModelAttribute QuestionFormDTO questionFormDTO,
											 @PathVariable Long id, 
											 Principal principal,
											 Model model) {
		Question question = questionService.findById(id);
		if(!question.getUsers().getUserName().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음");
		}
		questionFormDTO.setSubject(question.getSubject());
		questionFormDTO.setContent(question.getContent());
		
		return "question_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String modify(@Valid QuestionFormDTO questionFormDTO, BindingResult result,
											 @PathVariable Long id, Principal principal,
											 Model model) {
		if(result.hasErrors()) {
			return "question_form";
		}
		Question question = questionService.findById(id);
		if(!question.getUsers().getUserName().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음");
		}
		questionService.modify(question, questionFormDTO.getSubject(), questionFormDTO.getContent());
		return String.format("redirect:/question/detail/%s", id);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Long id, Principal principal) {
		Question question = questionService.findById(id);
		if(!question.getUsers().getUserName().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한 없음");
		}
		questionService.delete(question);
		return "redirect:/question/list";
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String vote(@PathVariable Long id,
										 Principal principal) {
		Question question = questionService.findById(id);
		Users user = usersService.getUsers(principal.getName());
		questionService.vote(question, user);
		return String.format("redirect:/question/detail/%s", question.getId());
	}

}
