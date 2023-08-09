package com.tjoeun.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tjoeun.dto.AnswerFormDTO;
import com.tjoeun.entity.Question;
import com.tjoeun.service.AnswerService;
import com.tjoeun.service.QuestionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/answer")
@RequiredArgsConstructor
public class AnswerController {
	
	private final QuestionService questionService;
	private final AnswerService answerService;
	
	@PostMapping("/create/{id}")
	public String createAnswer(@PathVariable Long id, Model model,
														 @Valid AnswerFormDTO answerFormDTO,
														 BindingResult result) {
		Question question = questionService.findById(id);
		if(result.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";
		}
		
		answerService.createAnswer(question, answerFormDTO.getContent());
		
		return String.format("redirect:/question/detail/%s", id);
	}

}
