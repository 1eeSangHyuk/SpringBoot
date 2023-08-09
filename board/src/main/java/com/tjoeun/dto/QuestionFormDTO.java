package com.tjoeun.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Valid
public class QuestionFormDTO {
	
	@NotEmpty(message = "질문 제목을 입력해주세요.")
	@Size(max = 100)
	private String subject;
	
	@NotEmpty(message = "질문 내용을 입력해주세요.")
	private String content;
}
