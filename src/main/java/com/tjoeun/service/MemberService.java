package com.tjoeun.service;

import java.util.Optional;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tjoeun.entity.Member;
import com.tjoeun.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor	// final 멤버변수를 초기화해주는 생성자
public class MemberService implements UserDetailsService{
	
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

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		//
		Member member = memberRepository.findByEmail(email);
		if(member == null) {
			throw new UsernameNotFoundException("헤당 이메일로 가입한 사용자가 존재하지 않습니다 - "+email);
		}
		
		//Optional
		//email로 회원 조회해서 로그인하기
//		Member member = memberRepository.findByEmail(email)
//										.orElseThrow(EntityNotFoundException::new);
//		if(!member.isPresent()) {
//			throw new UsernameNotFoundException("헤당 이메일로 가입한 사용자가 존재하지 않습니다 - "+email);
//		}
		
		return User.builder()
				   .username(member.getEmail())
				   .password(member.getPassword())
				   .roles(member.getRole().toString())
				   .build();
	}
	
}
