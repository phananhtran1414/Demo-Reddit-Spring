package com.vti.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.marlonlom.utilities.timeago.TimeAgo;
import com.vti.dto.PostRequest;
import com.vti.dto.PostResponse;
import com.vti.dto.SubredditDto;
import com.vti.exceptions.PostNotFoundException;
import com.vti.exceptions.SubredditNotFoundException;
import com.vti.mapper.PostMapper;
import com.vti.model.Post;
import com.vti.model.Subreddit;
import com.vti.model.User;
import com.vti.repository.CommentRepository;
import com.vti.repository.PostRepository;
import com.vti.repository.SubredditRepository;
import com.vti.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {
	
	private final SubredditRepository subredditRepository;
	private final AuthService authService;
	private final PostRepository postRepository;
	private final UserRepository userRepository;
	private final CommentRepository commentRepository;
	
	public void save(PostRequest postRequest) {
		
		Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
						.orElseThrow(() -> new SubredditNotFoundException(postRequest.getSubredditName()));
		
		User currentUser = authService.getCurrentUser();
		
		postRepository.save(mapDtoToPost(postRequest, subreddit, currentUser));
		
	}

	
	
 	@Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
	 
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
        
        return mapPostToDto(post);
    }

 	
    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(this::mapPostToDto)
                .collect(Collectors.toList());
    }
	
    
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long subredditId) {
        Subreddit subreddit = subredditRepository.findById(subredditId)
                .orElseThrow(() -> new SubredditNotFoundException(subredditId.toString()));
        
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        
        return posts.stream().map(this::mapPostToDto).collect(Collectors.toList());
    }

    
    @Transactional(readOnly = true)
    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        
        return postRepository.findByUser(user)
                .stream()
                .map(this::mapPostToDto)
                .collect(Collectors.toList());
    }
	
    
    
    
    private Post mapDtoToPost(PostRequest postRequest, Subreddit subreddit, User currentUser) {
		return Post.builder()
				.postId(postRequest.getPostId())
				.postName(postRequest.getPostName())
				.url(postRequest.getUrl())
				.description(postRequest.getDescription())
				.user(currentUser)
				.createdDate(Instant.now())
				.voteCount(0)
				.subreddit(subreddit)
				.build();
	}
    
	
	private PostResponse mapPostToDto(Post post) {
		return PostResponse.builder()
				.id(post.getPostId())
				.postName(post.getPostName())
				.url(post.getUrl())
				.description(post.getDescription())
				.userName(post.getUser().getUsername())
				.subredditName(post.getSubreddit().getName())
				.voteCount(post.getVoteCount())
				.commentCount(commentCount(post))
				.duration(getDuration(post))
				.build();
	}
	
	Integer commentCount(Post post) {
        return commentRepository.findByPost(post).size();
    }

    String getDuration(Post post) {
        return TimeAgo.using(post.getCreatedDate().toEpochMilli());
    }
}
