package com.tjoeun.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
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
	
	@GetMapping("/list")
	// @ResponseBody
	public String list(Model model,
										 @RequestParam(value = "page", defaultValue = "0") int page) {
		Page<Question> paging = questionService.findAll(page);
		model.addAttribute("paging", paging);
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
	public String create1(@Valid QuestionFormDTO questionFormDTO,
												BindingResult result,
												Principal principal) {
		if(result.hasErrors()) {
			return "question_form";
		}
		Users user = usersService.getUsers(principal.getName());
		questionService.save(questionFormDTO.getSubject() ,questionFormDTO.getContent(), user);
		return "redirect:/question/list";
	}
}
