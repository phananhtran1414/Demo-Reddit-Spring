package com.vti.service;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.vti.dto.SubredditDto;
import com.vti.exceptions.SpringRedditException;
import com.vti.mapper.SubredditMapper;
import com.vti.model.Subreddit;
import com.vti.repository.SubredditRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class SubredditService {

	private final SubredditRepository repo;
	
	@Transactional
	public SubredditDto save(SubredditDto subredditDto) {
		Subreddit subreddit = mapDtoToSubreddit(subredditDto);
		Subreddit save = repo.save(subreddit);
		
		subredditDto.setId(save.getId());
		
		return subredditDto;	
	}

	

	@Transactional(propagation = Propagation.REQUIRED, readOnly = true )
	public  List<SubredditDto> getAll() {
		// Convert to SubredditDTO rồi đổi sang List
		return repo.findAll()
				.stream()
				.map(this::mapToDto)
				.collect(Collectors.toList());
		
	}



	public SubredditDto getSubreddit(Long id) {
		
		Subreddit subreddit = repo.findById(id).orElseThrow(() -> new SpringRedditException("No subreddit found with ID - " + id));
		
		return mapToDto(subreddit);
	}
	
	private Subreddit mapDtoToSubreddit(SubredditDto subredditDto) {
		return Subreddit.builder()
				.name(subredditDto.getName())
				.description(subredditDto.getDescription())
				.build();
	}
	
	private SubredditDto mapToDto(Subreddit subreddit) {
		return SubredditDto.builder()
				.name(subreddit.getName())
				.id(subreddit.getId())
				.numberOfPosts(subreddit.getPosts().size())
				.build();
	}
}
