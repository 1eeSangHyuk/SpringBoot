package com.tjoeun.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tjoeun.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>{

	Question findBySubject(String subject);
	
	Question findBySubjectAndContent(String subject, String content);

	List<Question> findBySubjectLike(String subject);
	
	// 페이징 
	Page<Question> findAll(Pageable pageable);
	
	Page<Question> findAllByOrderByCreateDateDesc(Pageable pageable);
	// 페이징
	
	// 페이징+검색
	Page<Question> findAll(Specification<Question> spec, Pageable pageable);
	
	@Query("select "
			+ "distinct q "
			+ "from Question q "
			+ "left outer join Users u1 on q.users = u1 "
			+ "left outer join Answer a on a.question = q "
			+ "left outer join Users u2 on a.users = u2 "
			+ "where q.subject like %:keyword% "
			+ "or q.content like %:keyword% "
			+ "or u1.userName like %:keyword% "
			+ "or a.content like %:keyword% "
			+ "or u2.userName like %:keyword%")
	Page<Question> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
	// 페이징+검색
}
