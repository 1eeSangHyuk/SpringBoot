package com.tjoeun.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.tjoeun.constant.Role;
import com.tjoeun.dto.MemberFormDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;
	
	private String name;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	private String address;

	@Enumerated(EnumType.STRING)
	private Role role;
	
	// SecurityConfig 클래스에서 
	// 회원가입할 때 입력한 비밀번호 암호 처리한 것을
	// DB 에 적용하기
	public static Member createMember(MemberFormDTO memberDTO, PasswordEncoder passwordEncoder) {
		
		//	비밀번호 암호처리하기 - 암호화된 비밀번호를 Member Entity의 password 에 저장
		String password = passwordEncoder.encode(memberDTO.getPassword());
		
		Member member = Member.builder()
													.name(memberDTO.getName())
													.email(memberDTO.getEmail())
													.password(password)
													.address(memberDTO.getAddress())
													.role(Role.USER)
													.build();
		
		return member;
	}
	
}
