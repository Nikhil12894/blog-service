package com.nk.blog.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.nk.blog.constants.PostConstents;
import com.nk.blog.constants.Testconstants;
import com.nk.blog.dto.BlogPostDTO;
import com.nk.blog.dto.BlogPostListDTO;
import com.nk.blog.dto.BlogPostRequest;
import com.nk.blog.enums.BlogPostShortBy;
import com.nk.blog.enums.BlogStatus;
import com.nk.blog.enums.SortOrder;
import com.nk.blog.exception.BadRequestException;
import com.nk.blog.exception.CustomDataIntegrityViolationException;
import com.nk.blog.exception.DataConflictException;
import com.nk.blog.exception.InternalServerException;
import com.nk.blog.model.BlogPost;
import com.nk.blog.repo.BlogPostRepo;
import com.nk.blog.service.impl.BlogPostServiceImpl;

@ExtendWith(MockitoExtension.class)
class BlogPostServiceTest {

    private BlogPostService service;

    @Mock
    private BlogPostRepo blogRepository;

    @BeforeEach
    void setUp() {
        service = new BlogPostServiceImpl(blogRepository);

    }

    @Test
    void testCreateBlog_ValidRequest_ReturnsDTO() {
        // Arrange
        BlogPostRequest request = BlogPostRequest.builder()
                .title("Test Title")
                .description("Test Description")
                .content("Test Content")
                .imageUrl("Test Image URL")
                .status(BlogStatus.DRAFT)
                .build();
        // Mock
        when(blogRepository.findByTitle("Test Title")).thenReturn(Optional.empty());
        when(blogRepository.save(any(BlogPost.class))).thenReturn(BlogPost.builder()
                .title("Test Title")
                .description("Test Description")
                .content("Test Content")
                .imageUrl("Test Image URL")
                .status(BlogStatus.DRAFT)
                .id(1L)
                .createdAt(LocalDateTime.now())
                .createdBy(PostConstents.SYSTEM_USER)
                .build());
        // Act
        BlogPostDTO result = service.createBlog(request);

        // Assert
        assertEquals("Test Title", result.getTitle());
        assertEquals("Test Description", result.getDescription());
        assertEquals("Test Content", result.getContent());
        assertEquals("Test Image URL", result.getImageUrl());
        assertEquals(1L, result.getId());
        assertEquals(BlogStatus.DRAFT, result.getBlogStatus());
        assertNotNull(result.getCreatedAt());
        assertEquals(PostConstents.SYSTEM_USER, result.getCreatedBy());
        assertEquals(BlogStatus.DRAFT, result.getBlogStatus());
        // Verify
        verify(blogRepository, times(1)).findByTitle("Test Title");
        verify(blogRepository, times(1)).save(any(BlogPost.class));
        verifyNoMoreInteractions(blogRepository);
    }

    @Test
    void testCreateBlog_RequestWithExistingTitle() {
        // Arrange
        BlogPostRequest existingTitleRequest = BlogPostRequest.builder().title("Existing Title")
                .content("Existing Content").build();
        when(blogRepository.findByTitle(anyString())).thenReturn(
                Optional.of(BlogPost.builder().title("Existing Title").content("Existing Content").build()));

        // Act & Assert
        assertThrows(DataConflictException.class, () -> service.createBlog(existingTitleRequest));
    }

    @Test
    void testCreateBlog_DataIntegrityViolationException() {
        // Arrange
        BlogPostRequest validRequest = BlogPostRequest.builder().title("New Title").content("New Content").build();
        when(blogRepository.findByTitle(anyString())).thenReturn(Optional.empty());
        when(blogRepository.save(any(BlogPost.class))).thenThrow(DataIntegrityViolationException.class);

        // Act & Assert
        assertThrows(CustomDataIntegrityViolationException.class, () -> service.createBlog(validRequest));
    }

    @Test
    void testGetAllBlogsCurrentUser_ValidParameters_ReturnsBlogPostListDTO() {
        // Setup
        final BlogPostListDTO expectedResult = BlogPostListDTO.builder()
                .blogPostList(List.of(BlogPostDTO.builder()
                        .id(0L)
                        .title("title")
                        .content("content")
                        .description("description")
                        .imageUrl("imageUrl")
                        .blogStatus(BlogStatus.DRAFT)
                        .createdBy(0L)
                        .createdAt(Testconstants.DEFAULT_DATETIME)
                        .lastUpdatedBy(0L)
                        .lastUpdatedAt(Testconstants.DEFAULT_DATETIME)
                        .build()))
                .totalPages(1)
                .total(1L)
                .sortOrder(SortOrder.ASC)
                .sortBy(BlogPostShortBy.CREATED_AT)
                .build();

        // Configure ScheduleRepo.findAll(...).
        final Page<BlogPost> blogPage = new PageImpl<>(List.of(BlogPost.builder()
                .id(0L)
                .title("title")
                .content("content")
                .description("description")
                .imageUrl("imageUrl")
                .status(BlogStatus.DRAFT)
                .createdBy(0L)
                .createdAt(Testconstants.DEFAULT_DATETIME)
                .lastUpdatedBy(0L)
                .lastUpdatedAt(Testconstants.DEFAULT_DATETIME)
                .build()));
        when(blogRepository.findAllByCreatedBy(anyLong(), any(Pageable.class))).thenReturn(blogPage);

        // Run the test
        final BlogPostListDTO result = service.getAllBlogsCurrentUser(PostConstents.DEFAULT_PAGE,
                PostConstents.DEFAULT_PAGE_SIZE, SortOrder.ASC,
                BlogPostShortBy.CREATED_AT);

        // Verify the results
        assertEquals(result, expectedResult);
        // Verify
        verify(blogRepository, times(1)).findAllByCreatedBy(anyLong(), any(Pageable.class));
    }


    @Test
    void testGetAllBlogsCurrentUser_withPage_0_and_updated_to_1() {
              // Setup
        final BlogPostListDTO expectedResult = BlogPostListDTO.builder()
                .blogPostList(List.of(BlogPostDTO.builder()
                        .build()))
                .totalPages(1)
                .total(1L)
                .sortOrder(SortOrder.ASC)
                .sortBy(BlogPostShortBy.CREATED_AT)
                .build();

        // Configure ScheduleRepo.findAll(...).
        final Page<BlogPost> blogPage = new PageImpl<>(List.of(BlogPost.builder()
                .build()));
        when(blogRepository.findAllByCreatedBy(anyLong(), any(Pageable.class))).thenReturn(blogPage);
        // Run the test
        final BlogPostListDTO result = service.getAllBlogsCurrentUser(0,
         PostConstents.DEFAULT_PAGE_SIZE, SortOrder.ASC,
         BlogPostShortBy.CREATED_AT);
         // Act & Assert
         assertEquals(result, expectedResult);
    }

    @Test
    void testGetAllBlogsCurrentUser_withInvalidPageSize() {
         // Act & Assert
         assertThrows(BadRequestException.class, () -> service.getAllBlogsCurrentUser(PostConstents.DEFAULT_PAGE,
         -1, SortOrder.ASC,
         BlogPostShortBy.CREATED_AT));
    }


    @Test
    void testGetAllBlogsCurrentUser_withNullSortOrder() {
         // Act & Assert
         assertThrows(BadRequestException.class, () -> service.getAllBlogsCurrentUser(PostConstents.DEFAULT_PAGE,
         PostConstents.DEFAULT_PAGE_SIZE, null,
         BlogPostShortBy.CREATED_AT));
    }

    @Test
    void testGetAllBlogsCurrentUser_withNullOrderBy() {
         // Act & Assert
         assertThrows(BadRequestException.class, () -> service.getAllBlogsCurrentUser(PostConstents.DEFAULT_PAGE,
         PostConstents.DEFAULT_PAGE_SIZE, SortOrder.DESC,
         null));
    }

    @Test
    void testGetAllBlogsCurrentUser_withInvalidPageAndNotMockingRepo() {
         // Act & Assert
         assertThrows(InternalServerException.class, () -> service.getAllBlogsCurrentUser(0,
         PostConstents.DEFAULT_PAGE_SIZE, SortOrder.DESC,
         BlogPostShortBy.CREATED_AT));
    }

    @Test
    void testGetAllBlogsCurrentUser_withInvalidPageNumberOfTotalPage() {
              // Setup

        // Configure ScheduleRepo.findAll(...).
        final Page<BlogPost> blogPage = new PageImpl<>(List.of(BlogPost.builder()
                .build()));
        when(blogRepository.findAllByCreatedBy(anyLong(), any(Pageable.class))).thenReturn(blogPage);
        // Run the test
         // Act & Assert
         assertThrows(BadRequestException.class, () -> service.getAllBlogsCurrentUser(2,
         PostConstents.DEFAULT_PAGE_SIZE, SortOrder.ASC,
         BlogPostShortBy.CREATED_AT), "Invalid page number, number of available pages is 1");
    }

    @Test
    void testGetAllBlogsCurrentUser_withInvalidPageNumber() {
              // Setup
        final BlogPostListDTO expectedResult = BlogPostListDTO.builder()
                .blogPostList(Collections.EMPTY_LIST)
                .totalPages(null)
                .total(null)
                .sortOrder(null)
                .sortBy(null)
                .build();

        // Configure ScheduleRepo.findAll(...).
        final Page<BlogPost> blogPage = new PageImpl<>(Collections.EMPTY_LIST);
        when(blogRepository.findAllByCreatedBy(anyLong(), any(Pageable.class))).thenReturn(blogPage);
        // Run the test
        final BlogPostListDTO result = service.getAllBlogsCurrentUser(0,
         PostConstents.DEFAULT_PAGE_SIZE, SortOrder.ASC,
         BlogPostShortBy.CREATED_AT);
         // Act & Assert
         assertEquals(result, expectedResult);
    }
}
