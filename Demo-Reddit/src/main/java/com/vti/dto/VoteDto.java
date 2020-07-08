package com.vti.dto;

import com.vti.model.VoteType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VoteDto {
	
	private VoteType voteType;
	private Long postId;
	
}
