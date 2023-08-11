package com.tjoeun.dto;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Builder
@Valid
public class UsersFormDTO {
	
	@NotEmpty(message = "닉네임은 반드시 입력해주세요.")
	@Size(min = 4, max = 20, message = "닉네임은 4글자 이상, 20글자 이하로 입력해주세요.")
	private String userName;
	
	@NotEmpty(message = "비밀번호는 반드시 입력해주세요.")
	private String password1;
	
	@NotEmpty(message = "확인용 비밀번호는 반드시 입력해주세요.")
	private String password2;
	
	@NotEmpty(message = "이메일은 반드시 입력해주세요.")
	@Email(message = "이메일 형식으로 입력해주세요.")
	private String email;
}
