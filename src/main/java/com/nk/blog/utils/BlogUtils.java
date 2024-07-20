package com.nk.blog.utils;

import java.time.LocalDateTime;
import java.util.List;

import com.nk.blog.constants.CommonsConstants;
import com.nk.blog.constants.DateConstants;
import com.nk.blog.dto.BlogPostDTO;
import com.nk.blog.dto.BlogPostRequest;
import com.nk.blog.enums.BlogStatus;
import com.nk.blog.model.BlogPost;

public interface BlogUtils {

    public static List<BlogPostDTO> blogPostListToDTO(List<BlogPost> blogPostList) {
        return blogPostList.stream().map(BlogUtils::blogPostToDTO).toList();
    }
    public static BlogPostDTO blogPostToDTO(BlogPost blogPost) {
        return BlogPostDTO.builder()
        .blogStatus(blogPost.getStatus())
        .id(blogPost.getId())
        .title(blogPost.getTitle())
        .description(blogPost.getDescription())
        .content(blogPost.getContent())
        .blogStatus(blogPost.getStatus())
        .imageUrl(blogPost.getImageUrl())
        .createdBy(blogPost.getCreatedBy())
        .createdAt(blogPost.getCreatedAt())
        .build();
    }

    public static BlogPost blogPostDTOToBlogPost(BlogPostDTO blogPostDto) {
        return BlogPost.builder()
        .status(blogPostDto.getBlogStatus())
        .id(blogPostDto.getId())
        .title(blogPostDto.getTitle())
        .description(blogPostDto.getDescription())
        .content(blogPostDto.getContent())
        .status(blogPostDto.getBlogStatus())
        .imageUrl(blogPostDto.getImageUrl())
        .createdBy(blogPostDto.getCreatedBy())
        .createdAt(blogPostDto.getCreatedAt())
        .lastUpdatedBy(blogPostDto.getLastUpdatedBy())
        .lastUpdatedAt(blogPostDto.getLastUpdatedAt())
        .build();
    }

    public static BlogPost blogPostRequestToBlog(BlogPostRequest blogRequest) {
        return BlogPost.builder()
        .id(blogRequest.getId())
        .title(blogRequest.getTitle())
        .status(BlogStatus.DRAFT)
        .description(blogRequest.getDescription())
        .content(blogRequest.getContent())
        .imageUrl(blogRequest.getImageUrl())
        .build();
    }


    public static void setAuditField(BlogPost blogPost) {
        if(blogPost.getId() == null) {
            blogPost.setCreatedBy(getCurrentUserId());
            blogPost.setCreatedAt(LocalDateTime.now(DateConstants.DEFAULT_ZONEID));
        }else{
            blogPost.setLastUpdatedBy(getCurrentUserId());
            blogPost.setLastUpdatedAt(LocalDateTime.now(DateConstants.DEFAULT_ZONEID));
        }
    }

    public static long getCurrentUserId() {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // return authentication.getName();

        return CommonsConstants.SYSTEM_USER;
    }
}
