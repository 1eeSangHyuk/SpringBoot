package com.tjoeun.controller;

import java.security.Principal;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.tjoeun.dto.CommentFormDTO;
import com.tjoeun.entity.Answer;
import com.tjoeun.entity.Comment;
import com.tjoeun.entity.Question;
import com.tjoeun.entity.Users;
import com.tjoeun.service.AnswerService;
import com.tjoeun.service.CommentService;
import com.tjoeun.service.QuestionService;
import com.tjoeun.service.UsersService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
	
	private final QuestionService questionService;
	private final AnswerService answerService;
	private final UsersService usersService;
	private final CommentService commentService;
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create/question/{id}")
	public String createComment(@PathVariable Long id,
																CommentFormDTO commentFormDTO) {
		return "comment_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/question/{id}")
	public String createqComment(@PathVariable Long id,
																@Valid CommentFormDTO commentFormDTO,
																BindingResult result,
																Principal principal) {
		if(result.hasErrors()) {
			return "comment_form";
		}
		
		Question question = questionService.findById(id);
		Users user = usersService.getUsers(principal.getName());
		Comment comment = commentService.create(question, user, commentFormDTO.getContent());
		
		return String.format("redirect:/question/detail/%s", comment.getQuestionId());
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create/answer/{id}")
	public String createaComment(@PathVariable Long id,
															 CommentFormDTO commentFormDTO) {
		return "comment_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/answer/{id}")
	public String createaComment(@PathVariable Long id,
																@Valid CommentFormDTO commentFormDTO,
																BindingResult result,
																Principal principal) {
		if(result.hasErrors()) {
			return "comment_form";
		}
		
		Answer answer = answerService.findById(id);
		Users user = usersService.getUsers(principal.getName());
		Comment comment = commentService.create(answer, user, commentFormDTO.getContent());
		
		if(comment.getQuestion() != null) {
			return String.format("redirect:/question/detail/%s", comment.getQuestionId());
		} else {
			return String.format("redirect:/question/detail/%s#answer_%s", comment.getQuestionId(), comment.getAnswer().getId());
		}
		
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String modifyComment(CommentFormDTO commentFormDTO,
															@PathVariable Long id,
															Principal principal) {
		Optional<Comment> c = commentService.getComment(id);
		if(c.isPresent()) {
			Comment comment = c.get();
			if(!comment.getUsers().getUserName().equals(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음");
			}
			commentFormDTO.setContent(comment.getContent());
		}
		return "comment_form";
	}
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String modifyComment(@Valid CommentFormDTO commentFormDTO,
															BindingResult result,
															@PathVariable Long id,
															Principal principal) {
		if(result.hasErrors()) {
			return "comment_form"; 
		}
		Optional<Comment> c = commentService.getComment(id);
		if(c.isPresent()) {
			Comment comment = c.get();
			if(!comment.getUsers().getUserName().equals(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 수정 권한 없음");
			}
			commentService.modify(comment, commentFormDTO.getContent());
			if(comment.getQuestion() != null) {
				return String.format("redirect:/question/detail/%s", comment.getQuestionId());
			} else {
				return String.format("redirect:/question/detail/%s#answer_%s", comment.getQuestionId(), comment.getAnswer().getId());
			}
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "수정할 댓글이 존재하지 않습니다.");
		}
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String deleteComment(@PathVariable Long id,
															Principal principal) {
		Optional<Comment> c = commentService.getComment(id);
		if(c.isPresent()) {
			Comment comment = c.get();
			if(!comment.getUsers().getUserName().equals(principal.getName())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "댓글 삭제 권한 없음");
			}
			commentService.delete(comment);
			return String.format("redirect:/question/detail/%s", comment.getQuestionId());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "삭제할 댓글이 존재하지 않습니다.");
		}
	}
}
