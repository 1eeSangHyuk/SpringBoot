package com.tjoeun.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@Valid
public class CommentFormDTO {
	
	@NotEmpty(message = "댓글을 입력한 후 등록해 주십시오.")
	private String content;
}
