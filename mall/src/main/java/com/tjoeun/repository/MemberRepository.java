package com.tjoeun.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tjoeun.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	
	//	query method
	public Member findByEmail(String email);
	
	// query method + Optional
	//public Optional<Member> findByEmail(String email);
	
	
}
