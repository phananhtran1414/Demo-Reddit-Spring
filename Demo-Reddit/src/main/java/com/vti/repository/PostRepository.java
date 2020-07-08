package com.vti.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vti.model.Post;
import com.vti.model.Subreddit;
import com.vti.model.User;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
	
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}