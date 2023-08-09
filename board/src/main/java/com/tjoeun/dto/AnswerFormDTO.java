package com.tjoeun.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

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
public class AnswerFormDTO {
	
	@NotEmpty(message = "답변 내용을 입력해주세요.")
	private String content;
}
