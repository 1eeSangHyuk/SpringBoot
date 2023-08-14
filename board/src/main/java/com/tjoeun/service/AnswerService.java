package com.tjoeun.service;

import java.time.LocalDateTime;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import com.tjoeun.entity.Answer;
import com.tjoeun.entity.Question;
import com.tjoeun.entity.Users;
import com.tjoeun.repository.AnswerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerService {

	private final AnswerRepository answerRepository;
	
	public Answer createAnswer(Question question, String content, Users users) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setQuestion(question);
		answer.setUsers(users);
		
		answer = answerRepository.save(answer);
		return answer;
	}
	
	public Answer findById(Long id) {
		return answerRepository.findById(id)
													 .orElseThrow(EntityNotFoundException::new);
	}
	
	public void modify(Answer answer, String content) {
		answer.setContent(content);
		answer.setModifyDate(LocalDateTime.now());
		answerRepository.save(answer);
	}
	
	public void delete(Answer answer) {
		answerRepository.delete(answer);
	}
	
	public void vote(Answer answer, Users users) {
		answer.getVoter().add(users);
		answerRepository.save(answer);
	}
}
