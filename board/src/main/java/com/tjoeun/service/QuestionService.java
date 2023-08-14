package com.tjoeun.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.tjoeun.entity.Answer;
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
	
	public void modify(Question question, String subject, String content) {
		question.setSubject(subject);
		question.setContent(content);
		question.setModifyDate(LocalDateTime.now());
		questionRepository.save(question);
	}
	
	public void delete(Question question) {
		questionRepository.delete(question);
	}
	
	public void vote(Question question, Users users) {
		question.getVoter().add(users);
		questionRepository.save(question);
	}
	
	// 페이징 + 검색
	public Page<Question> findAll(int page, String keyword){
		// 페이징 + 검색 1
//		 List<Sort.Order> sorts = new ArrayList<>();
//		 sorts.add(Sort.Order.desc("createDate"));
//		 Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
//		 Specification<Question> spec = search(keyword);
//		 Page<Question> paging = questionRepository.findAll(spec, pageable);
		
		// 페이징 + 검색 2
    List<Sort.Order> sorts = new ArrayList<>();
	  sorts.add(Sort.Order.desc("createDate"));
	  Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		Page<Question> paging = questionRepository.findAllByKeyword(keyword, pageable);
		return paging;
	}
	
	// 검색 - JPA 에서 제공하는 Specification 인터페이스 사용
	private Specification<Question> search(String keyword){
		Specification<Question> spec = new Specification<Question>() {

			private static final long serialVersionUID = -6075709197056457891L;

			@Override
			public Predicate toPredicate(Root<Question> question, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				query.distinct(true);
				Join<Question, Users> qu = question.join("users", JoinType.LEFT);
				Join<Question, Answer> qa = question.join("answerList", JoinType.LEFT);
				Join<Answer, Users> au = question.join("users", JoinType.LEFT);
				
				/*
				 * 제목 / 내용 / 질문글쓴이 / 답글내용 / 답글글쓴이
				 */
				return criteriaBuilder.or(criteriaBuilder.like(question.get("subject"), "%"+ keyword + "%"),
																  criteriaBuilder.like(question.get("content"), "%"+ keyword + "%"),
																  criteriaBuilder.like(qu.get("userName"), "%"+ keyword + "%"),
																  criteriaBuilder.like(qa.get("content"), "%"+ keyword + "%"),
																  criteriaBuilder.like(au.get("userName"), "%"+ keyword + "%"));
			}
		};
		
		return spec;
	}
}
