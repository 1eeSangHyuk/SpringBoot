package com.tjoeun.controller;

import java.security.Principal;

import javax.validation.Valid;

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
import org.springframework.web.server.ResponseStatusException;

import com.tjoeun.dto.AnswerFormDTO;
import com.tjoeun.entity.Answer;
import com.tjoeun.entity.Question;
import com.tjoeun.entity.Users;
import com.tjoeun.service.AnswerService;
import com.tjoeun.service.QuestionService;
import com.tjoeun.service.UsersService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {
	
	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UsersService usersService;
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}")
	public String createAnswer(@PathVariable Long id, Model model,
														 @Valid AnswerFormDTO answerFormDTO,
														 BindingResult result,
														 Principal principal) {
		Question question = questionService.findById(id);
		if(result.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";
		}
		
		Users user = usersService.getUsers(principal.getName());
		Answer answer = answerService.createAnswer(question, answerFormDTO.getContent(), user);
		
		return String.format("redirect:/question/detail/%s#answer_%s", id, answer.getId());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{aid}")
	public String modify(@ModelAttribute AnswerFormDTO answerFormDTO,
											 @PathVariable("aid") Long aid, 
											 Principal principal,
											 Model model) {
		Answer answer = answerService.findById(aid);
		if(!answer.getUsers().getUserName().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음");
		}
		answerFormDTO.setContent(answer.getContent());

		return "answer_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{aid}")
	public String modify(@Valid AnswerFormDTO answerFormDTO, BindingResult result,
											 @PathVariable("aid") Long aid,
											 Principal principal,
											 Model model) {
		if(result.hasErrors()) {
			return "answer_form";
		}
		Answer answer = answerService.findById(aid);
		if(!answer.getUsers().getUserName().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음");
		}
		answerService.modify(answer, answerFormDTO.getContent());
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{aid}")
	public String delete(@PathVariable("aid") Long aid, Principal principal) {
		Answer answer = answerService.findById(aid);
		if(!answer.getUsers().getUserName().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한 없음");
		}
		answerService.delete(answer);
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String vote(@PathVariable Long id,
										 Principal principal) {
		Answer answer = answerService.findById(id);
		Users user = usersService.getUsers(principal.getName());
		answerService.vote(answer, user);
		return String.format("redirect:/question/detail/%s#answer_%s", answer.getQuestion().getId(), answer.getId());
	}

}
