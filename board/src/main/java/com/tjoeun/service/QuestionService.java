package com.tjoeun.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.tjoeun.entity.Question;
import com.tjoeun.entity.Users;
import com.tjoeun.repository.QuestionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {
	
	private final QuestionRepository questionRepository;
	
	public Page<Question> findAll(int page) {
		
		
		// 최근 작성순 정렬 1
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
	  //페이징 처리
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Page<Question> questionPage = questionRepository.findAll(pageable);
		
		// 최근 작성순 정렬 2
		// 페이징 처리
//		Pageable pageable = PageRequest.of(page, 10);
//		Page<Question> questionPage = questionRepository.findAllByOrderByCreateDateDesc(pageable);
		return questionPage;
	}
	
	public Question findById(Long id) {
		Question question = questionRepository.findById(id)
																					.orElseThrow(EntityNotFoundException::new);
		return question;
	}
	
	public void save(String subject, String content, Users users) {
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreateDate(LocalDateTime.now());
		question.setUsers(users);
		questionRepository.save(question);
	}
}
