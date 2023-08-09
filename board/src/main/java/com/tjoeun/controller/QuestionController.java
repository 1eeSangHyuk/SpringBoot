package com.tjoeun.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tjoeun.dto.AnswerFormDTO;
import com.tjoeun.dto.QuestionFormDTO;
import com.tjoeun.entity.Question;
import com.tjoeun.service.QuestionService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
	
	private final QuestionService questionService;
	
	@GetMapping("/list")
	// @ResponseBody
	public String list(Model model) {
		List<Question> questionList = questionService.findAll();
		model.addAttribute("questionList", questionList);
		return "question_list";
	}
	
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable("id") Long id, Model model,
											 @ModelAttribute AnswerFormDTO answerFormDTO) {
		
		Question question = questionService.findById(id);
		model.addAttribute("question", question);
		
		return "question_detail";
	}
	
	@GetMapping("/create")
	public String create(@ModelAttribute QuestionFormDTO questionFormDTO) {
		return "question_form";
	}
	
	@PostMapping("/create")
	public String create1(@Valid QuestionFormDTO questionFormDTO,
												BindingResult result) {
		if(result.hasErrors()) {
			return "question_form";
		}
		questionService.save(questionFormDTO.getSubject() ,questionFormDTO.getContent());
		return "redirect:/question/list";
	}
}
