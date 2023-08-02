package com.tjoeun.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tjoeun.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	
	//	query method
	public Member findByEmail(String email);
	
	
}
