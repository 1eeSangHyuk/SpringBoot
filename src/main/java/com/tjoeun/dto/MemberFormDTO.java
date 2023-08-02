package com.tjoeun.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Valid
public class MemberFormDTO {
	
	// 회원가입 시 입력해야 되는 내용을
	// MemberFormDTO 클래스의 멤버변수로 지정함
	// name, email, password, address
	
	@NotBlank(message = "이름을 입력해 주세요.")
	private String name;
	
	@NotEmpty(message = "이메일을 입력해 주세요.")
	@Email(message = "올바른 이메일 형식으로 입력해 주세요.")
	private String email;
	
	@NotBlank(message = "비밀번호를 입력해 주세요.")
	@Length(min = 4, max = 20, message = "비밀번호는 4글자 이상 20글자 이하로 입력해 주세요.")
	private String password;
	
	
	private String address;
	
}
