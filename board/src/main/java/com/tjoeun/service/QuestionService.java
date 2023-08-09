package com.tjoeun.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.tjoeun.entity.Question;
import com.tjoeun.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	
	public List<Question> findAll() {
		List<Question> questionList = questionRepository.findAll();
		return questionList;
	}
	
	public Question findById(Long id) {
		Question question = questionRepository.findById(id)
																					.orElseThrow(EntityNotFoundException::new);
		return question;
	}
	
	public void save(String subject, String content) {
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreateDate(LocalDateTime.now());
		questionRepository.save(question);
	}
}
