package com.vti.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vti.dto.CommentsDto;

import com.vti.exceptions.PostNotFoundException;
import com.vti.model.Comment;
import com.vti.model.NotificationEmail;
import com.vti.model.Post;

import com.vti.model.User;
import com.vti.repository.CommentRepository;
import com.vti.repository.PostRepository;
import com.vti.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CommentService {

	private static final String POST_URL = "";
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final AuthService authService;
	private final CommentRepository commentRepository;
	private final MailContentBuilder mailContentBuilder;
	private final MailService mailService;
	
	public void save(CommentsDto commentsDto) {
		Post post = postRepository.findById(commentsDto.getPostId())
						.orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
		User currentUser = authService.getCurrentUser();
		commentsDto.setCreatedDate(Instant.now());
		
		commentRepository.save(mapDtoToComment(commentsDto, post, currentUser));
		
		// Sau khi comment thì gửi mail thông báo chủ post
		String message = mailContentBuilder.build(post.getUser().getUsername() + " posted a comment on your post." + POST_URL);
		sendCommentNotification(message , post.getUser());
	}



	public List<CommentsDto> getAllCommentsForPost(Long postId) {
		
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new PostNotFoundException(postId.toString()));
		
		return commentRepository.findByPost(post)
					.stream()
					.map(this::mapCommentToDto)
					.collect(Collectors.toList());
		
		
	}

	public List<CommentsDto> getAllCommentsForUser(String username) {
		
		User user = userRepository.findByUsername(username)
						.orElseThrow(() -> new UsernameNotFoundException(username));
		
		return commentRepository.findAllByUser(user)
					.stream()
					.map(this::mapCommentToDto)
					.collect(Collectors.toList());
	}

	
	
	private void sendCommentNotification(String message, User user) {
		
		mailService.sendMail( new NotificationEmail(user.getUsername() + " commented on your post" , user.getEmail(), message));
		
	}
	
	private Comment mapDtoToComment(CommentsDto commentDto, Post post, User currentUser) {
		return Comment.builder()
				.text(commentDto.getText())
				.post(post)
				.createdDate(commentDto.getCreatedDate())
				.user(currentUser)
				.build();
	}
    
	
	private CommentsDto mapCommentToDto(Comment comment) {
		return CommentsDto.builder()
				.id(comment.getId())
				.postId(comment.getPost().getPostId())
				.createdDate(comment.getCreatedDate())
				.text(comment.getText())
				.userName(comment.getUser().getUsername())
				.build();
	}

}
