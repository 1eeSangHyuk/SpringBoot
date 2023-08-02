package com.tjoeun.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tjoeun.entity.Member;
import com.tjoeun.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor	// final 멤버변수를 초기화해주는 생성자
public class MemberService {
	
//	@Autowired
	private final MemberRepository memberRepository;
	
	public Member findByEmail(String email) {
		Member member = memberRepository.findByEmail(email);
		return member;
	}
	
	public Member saveMember(Member member) {
		// 가입하려는 회원과 같은 회원이 있는지 검증하는 Method 호출
		validateDuplicate(member);
		
		Member savedMember = memberRepository.save(member);
		return savedMember;
	}
	
	//	가입하려는 회원과 같은 회원이 있는지 검증하는 Method 호출
	private void validateDuplicate(Member member) {
		Member tmpMember = memberRepository.findByEmail(member.getEmail());
		if(tmpMember != null) {
			throw new IllegalStateException("동일 이메일로 가입된 회원이 존재합니다.");
		}
	}
	
}
