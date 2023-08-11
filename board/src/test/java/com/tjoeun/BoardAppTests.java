package com.tjoeun;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityNotFoundException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.tjoeun.entity.Answer;
import com.tjoeun.entity.Question;
import com.tjoeun.entity.Users;
import com.tjoeun.repository.AnswerRepository;
import com.tjoeun.repository.QuestionRepository;
import com.tjoeun.service.QuestionService;
import com.tjoeun.service.UsersService;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
//@Transactional
@Slf4j
class BoardAppTests {
	
	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private UsersService usersService;
	
	@Autowired
	private AnswerRepository answerRepository;
		
	@Test
	@DisplayName("질문 테스트1")
	void contextLoads() {
		Question q1 = new Question();
		q1.setSubject("질문1");
		q1.setContent("질문1 입니다.");
		q1.setCreateDate(LocalDateTime.now());
		questionRepository.save(q1);
		
		Question q2 = new Question();
		q2.setSubject("질문2");
		q2.setContent("질문2 입니다.");
		q2.setCreateDate(LocalDateTime.now());
		questionRepository.save(q2);
	}
	
	@Test
	@DisplayName("조회 테스트1")
	void selectTest1() {
		List<Question> questionList = questionRepository.findAll();
		
		assertEquals(2, questionList.size());
		
		Question q1 = questionList.get(0);
		assertEquals("질문1", q1.getSubject(), "조회 실패");
		
	}
	
	@Test
	@DisplayName("조회 테스트2")
	void selectTest2() {
		Optional<Question> questionOne = questionRepository.findById((long)1);
		if(questionOne.isPresent()) {
			Question q1 = questionOne.get();
			assertEquals("질문1 입니다.", q1.getContent(), "조회 실패");
		}
	}
	
	@Test
	@DisplayName("조회 테스트3")
	void selectTest3() {
		Question questionOne = questionRepository.findBySubjectAndContent("질문1", "질문1 입니다.");
		assertEquals(1, questionOne.getId(), "조회 실패");
	}
	
	@Test
	@DisplayName("조회 테스트4")
	void selectTest4() {
		List<Question> questionList = questionRepository.findBySubjectLike("질문%");
		assertEquals(1, questionList.get(0).getId(), "조회 실패");
	}
	
	@Test
	@DisplayName("수정 테스트 - 1")
	void updateTest1() {
		Optional<Question> q1 = questionRepository.findById((long)1);
		
		assertTrue(q1.isPresent());
			
		Question question = q1.get();
		question.setSubject("질문1-수정");
		questionRepository.save(question);
		
	}
	
	@Test
	@DisplayName("삭제 테스트 - 1")
	void deleteTest1() {
		
		assertEquals(2, questionRepository.count());
		
		Optional<Question> q1 = questionRepository.findById((long)1);
		assertTrue(q1.isPresent());
		Question question = q1.get();
		questionRepository.delete(question);
		
		assertEquals(1, questionRepository.count());
		
	}
	
	@Test
	@DisplayName("답변 테스트 - 1")
	void answerTest1() {
		Optional<Question> q1 = questionRepository.findById((long)2);
		assertTrue(q1.isPresent());
		Question question = q1.get();
		
		Answer a1 = new Answer();
		a1.setContent("질문2의 답글1");
		a1.setCreateDate(LocalDateTime.now());
		a1.setQuestion(question);
		
		answerRepository.save(a1);
	}
	
	@Test
	@DisplayName("답변 조회 테스트 - 1")
	void answerTest2() {
		Answer a1 = answerRepository.findById((long)1)
																.orElseThrow(EntityNotFoundException::new);
		assertEquals(2, a1.getQuestion().getId(), "답변 조회 실패");
	}
	
	@Test
	@DisplayName("질문 - 답변 조회 테스트 - 1")
	void questionAnswerTest1() {
		
		Question q1 = questionRepository.findById((long)2)
																		.orElseThrow(EntityNotFoundException::new);
		
		Answer a1 = answerRepository.findById((long)1)
																.orElseThrow(EntityNotFoundException::new);
		
		assertEquals("질문2의 답글1", a1.getContent(), "답변 조회 실패");
		assertEquals(q1.getId(), a1.getQuestion().getId(), "질문글의 답변인지 조회 실패");
		log.info(">>>>>>>>>>>>>>>>>>>>>>>a1.getQuestion().getId() : "+a1.getQuestion().getId());
		log.info(">>>>>>>>>>>>>>>>>>>>>>>q1.getId() : "+q1.getId());
	}
	
	@Test
	@DisplayName("질문글 한꺼번에 올리기")
	void uploadBoardTest () {
		for(int i=0; i<500; i++) {
			String subject = String.format("테스트 게시물 : [%03d]", i);
			String content = String.format("여기는 테스트 게시글 [%03d] 입니다", i);
			
			questionService.save(subject, content, null);
		}
	}
}
