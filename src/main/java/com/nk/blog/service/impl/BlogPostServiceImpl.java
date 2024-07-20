package com.nk.blog.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.nk.blog.dto.BlogPostDTO;
import com.nk.blog.dto.BlogPostListDTO;
import com.nk.blog.dto.BlogPostRequest;
import com.nk.blog.enums.BlogPostShortBy;
import com.nk.blog.enums.SortOrder;
import com.nk.blog.exception.BadRequestException;
import com.nk.blog.exception.CustomDataIntegrityViolationException;
import com.nk.blog.exception.DataConflictException;
import com.nk.blog.exception.InternalServerException;
import com.nk.blog.exception.NotFoundException;
import com.nk.blog.exception.UnAuthorizedException;
import com.nk.blog.model.BlogPost;
import com.nk.blog.repo.BlogPostRepo;
import com.nk.blog.service.BlogPostService;
import com.nk.blog.utils.BlogUtils;
import com.nk.blog.utils.Util;
import com.nk.blog.utils.Util.PagePageSizeRecord;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogPostServiceImpl implements BlogPostService {

  private final BlogPostRepo blogPostRepo;

  /**
   * Creates a new blog post based on the provided request.
   *
   * @param blogRequest the request containing the details of the blog post to
   *                    be created
   * @return the DTO representation of the created blog post
   *         +
   */
  @Override
  public BlogPostDTO createBlog(BlogPostRequest blogRequest) {
    try {
      validateBlogRequest(blogRequest);
      BlogPost blogToSave = BlogUtils.blogPostRequestToBlog(blogRequest);
      BlogUtils.setAuditField(blogToSave);
      blogToSave = blogPostRepo.save(blogToSave);
      return BlogUtils.blogPostToDTO(blogToSave);
    } catch (DataIntegrityViolationException e) {
      log.error("Failed to create blog due to data integrity violation: {}", e.getMessage());
      throw new CustomDataIntegrityViolationException(
          "Failed to create blog due to data integrity violation: " + e.getMessage());
    }
  }

  private void validateBlogRequest(BlogPostRequest blogRequest) {
    Optional<BlogPost> blogOptional = blogPostRepo.findByTitle(blogRequest.getTitle());
    if (blogOptional.isPresent()) {
      throw new DataConflictException("Blog already exists with this exact title");
    }
  }

  /**
   * Retrieves all blog posts created by the current user.
   *
   * @param page     the page number of the results (optional)
   * @param pageSize the number of results per page (optional)
   * @param sort     the sort order of the results (optional)
   * @param sortBy   the field to sort the results by (optional)
   * @return a DTO containing the list of blog posts
   * @throws BadRequestException     if the request parameters are invalid
   * @throws InternalServerException if an error occurs while retrieving the
   *                                 blog posts
   *                                 +
   */
  @Override
  public BlogPostListDTO getAllBlogsCurrentUser(
      Integer page,
      Integer pageSize,
      SortOrder sort,
      BlogPostShortBy sortBy) {
    Long userId = BlogUtils.getCurrentUserId();
    validateUser(userId);
    BlogPostListDTO.BlogPostListDTOBuilder builder = BlogPostListDTO
        .builder()
        .blogPostList(new ArrayList<>());

    try {
      // if no page or pagesize specified return all posts for current user
      PagePageSizeRecord validatedPagePageSize = Util.getResult(page, pageSize);
      Page<BlogPost> pagedSchedules = blogPostRepo.findAllByCreatedBy(userId,
          Util.getPageable(
              validatedPagePageSize.page(),
              validatedPagePageSize.pageSize(),
              null != sort ? sort.name() : null,
              null != sortBy ? sortBy.getOrderBy() : null));
      this.validateAndAddDataToListBuilder(
          sort,
          sortBy,
          builder,
          validatedPagePageSize,
          pagedSchedules);
    } catch (BadRequestException e) {
      throw e;
    } catch (Exception e) {
      log.error("Exception occurred while getting posts. {}", e);
      throw new InternalServerException(
          "Exception occurred while getting posts.");
    }
    return builder.build();
  }

  private void validateUser(Long userId) {
    // TODO if user not found throw exception
    if (null == userId) {
      throw new UnAuthorizedException("User not found");
    }
  }

  /**
   * Validates and adds data to the ScheduleDTOListBuilder based on sorting
   * criteria.
   *
   * @param sort                  the sort order to be applied
   * @param sortBy                the criteria to sort schedules by
   * @param listBuilder           the ScheduleDTOListBuilder to add schedules to
   * @param validatedPagePageSize the page and page size record
   * @param pagedBlogPost         the paged schedules to process
   */
  private void validateAndAddDataToListBuilder(
      SortOrder sort,
      BlogPostShortBy sortBy,
      BlogPostListDTO.BlogPostListDTOBuilder listBuilder,
      PagePageSizeRecord validatedPagePageSize,
      Page<BlogPost> pagedBlogPost) {
    if (validatedPagePageSize.page() > pagedBlogPost.getTotalPages()) {
      throw new BadRequestException(
          "Invalid page number, number of available pages is " +
              pagedBlogPost.getTotalPages());
    }
    if (pagedBlogPost.hasContent()) {
      List<BlogPostDTO> blogPostList = BlogUtils.blogPostListToDTO(
          pagedBlogPost.getContent());
      listBuilder
          .blogPostList(blogPostList)
          .total(pagedBlogPost.getTotalElements())
          .totalPages(pagedBlogPost.getTotalPages())
          .sortBy(sortBy)
          .sortOrder(sort);
    }
  }

  /**
   * Retrieves all blog posts based on specified parameters.
   *
   * @param page     the page number of the results
   * @param pageSize the number of results per page
   * @param sort     the sort order of the results
   * @param sortBy   the field to sort the results by
   * @return a DTO containing the list of blog posts
   *         +
   */
  @Override
  public BlogPostListDTO getAllBlogs(
      Integer page,
      Integer pageSize,
      SortOrder sort,
      BlogPostShortBy sortBy) {
    BlogPostListDTO.BlogPostListDTOBuilder builder = BlogPostListDTO
        .builder()
        .blogPostList(new ArrayList<>());

    try {
      // if no page or pagesize specified return all posts
      PagePageSizeRecord validatedPagePageSize = Util.getResult(page, pageSize);
      Page<BlogPost> pagedSchedules = blogPostRepo.findAll(
          Util.getPageable(
              validatedPagePageSize.page(),
              validatedPagePageSize.pageSize(),
              null != sort ? sort.name() : null,
              null != sortBy ? sortBy.getOrderBy() : null));
      this.validateAndAddDataToListBuilder(
          sort,
          sortBy,
          builder,
          validatedPagePageSize,
          pagedSchedules);
    } catch (BadRequestException e) {
      throw e;
    } catch (Exception e) {
      log.error("Exception occurred while getting posta. {}", e);
      throw new InternalServerException(
          "Exception occurred while getting posts.");
    }
    return builder.build();
  }

  /**
   * Retrieves a blog post by its ID.
   *
   * @param id the ID of the blog post
   * @return a DTO containing the blog post data
   * @throws NotFoundException if the blog post with the given ID is not found
   *
   */
  @Override
  public BlogPostDTO getBlogById(Long id) {
    Optional<BlogPost> blogOptional = blogPostRepo.findById(id);
    return blogOptional
        .map(BlogUtils::blogPostToDTO)
        .orElseThrow(() -> new NotFoundException(
            "Blog Post not found may be unpublished id: " + id));
  }

  @Override
  public BlogPostDTO updateBlog(Long id, BlogPostRequest blog) {
    try{
    Optional<BlogPost> blogOptional = blogPostRepo.findById(id);
    if (!blogOptional.isPresent()) {
      throw new NotFoundException(
          "Blog Post not found may be unpublished id: " + id);
    }
    BlogPost blogPost = blogOptional.get();
    BlogUtils.setAuditField(blogPost);
    blogPost.setId(id);
    blogPost.setTitle(blog.getTitle());
    blogPost.setDescription(blog.getDescription());
    blogPost.setContent(blog.getContent());
    blogPost.setImageUrl(blog.getImageUrl());
    blogPost.setStatus(blog.getStatus());
    BlogUtils.setAuditField(blogPost);
    blogPostRepo.save(blogPost);
    return BlogUtils.blogPostToDTO(blogPost);
    } catch (DataIntegrityViolationException e) {
      log.error("Data integrity violation while updating blog. {}", e.getMessage());
      throw new CustomDataIntegrityViolationException("Failed to Update blog due to data integrity violation: " + e.getMessage());
    }
  }

  @Override
  public void deleteBlog(Long id) {
    Optional<BlogPost> blogOptional = blogPostRepo.findById(id);
    blogOptional.ifPresent(blogPostRepo::delete);
  }
}
