package com.nk.blog.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.nk.blog.constants.CommonsConstants;
import com.nk.blog.dto.BlogPostDTO;
import com.nk.blog.dto.BlogPostRequest;
import com.nk.blog.enums.BlogStatus;
import com.nk.blog.exception.CustomDataIntegrityViolationException;
import com.nk.blog.exception.DataConflictException;
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
                .createdBy(CommonsConstants.SYSTEM_USER)
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
        assertEquals(CommonsConstants.SYSTEM_USER, result.getCreatedBy());
        assertEquals(BlogStatus.DRAFT, result.getBlogStatus());
        // Verify
        verify(blogRepository, times(1)).findByTitle("Test Title");
        verify(blogRepository, times(1)).save(any(BlogPost.class));
        verifyNoMoreInteractions(blogRepository);
    }

    @Test
    void testCreateBlog_RequestWithExistingTitle() {
        // Arrange
        BlogPostRequest existingTitleRequest = BlogPostRequest.builder().title("Existing Title").content("Existing Content").build();
        when(blogRepository.findByTitle(anyString())).thenReturn(Optional.of(BlogPost.builder().title("Existing Title").content ("Existing Content").build()));

        // Act & Assert
        assertThrows(DataConflictException.class, () -> service.createBlog(existingTitleRequest));
    }

    @Test
    void testCreateBlog_DataIntegrityViolationException() {
        // Arrange
        BlogPostRequest validRequest = BlogPostRequest.builder().title("New Title").content( "New Content").build();
        when(blogRepository.findByTitle(anyString())).thenReturn(Optional.empty());
        when(blogRepository.save(any(BlogPost.class))).thenThrow(DataIntegrityViolationException.class);

        // Act & Assert
        assertThrows(CustomDataIntegrityViolationException.class, () -> service.createBlog(validRequest));
    }

    

    
}
