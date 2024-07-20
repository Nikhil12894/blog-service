package com.nk.blog.service;

import com.nk.blog.dto.BlogPostDTO;
import com.nk.blog.dto.BlogPostListDTO;
import com.nk.blog.dto.BlogPostRequest;
import com.nk.blog.enums.BlogPostShortBy;
import com.nk.blog.enums.SortOrder;

public interface BlogPostService {
    BlogPostDTO createBlog(BlogPostRequest blogRequest);
    BlogPostListDTO getAllBlogsCurrentUser(Integer page, Integer pageSize, SortOrder sort, BlogPostShortBy sortBy);
    BlogPostListDTO getAllBlogs(Integer page, Integer pageSize, SortOrder sort, BlogPostShortBy sortBy);
    BlogPostDTO getBlogById(Long id);
    BlogPostDTO updateBlog(Long id, BlogPostRequest blog);
    void deleteBlog(Long id);
}
