package com.nk.blog.repo;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.nk.blog.model.BlogPost;

public interface BlogPostRepo extends JpaRepository<BlogPost, Long>{
    

	Page<BlogPost> findAllByCreatedBy(Long userId,Pageable pageable);

    Optional<BlogPost> findByTitle(String title);
}
