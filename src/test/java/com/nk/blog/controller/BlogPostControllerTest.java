package com.nk.blog.controller;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nk.blog.config.JacksonConfiguration;
import com.nk.blog.constants.Testconstants;
import com.nk.blog.dto.BlogPostDTO;
import com.nk.blog.dto.BlogPostListDTO;
import com.nk.blog.dto.BlogPostRequest;
import com.nk.blog.dto.WebResponse;
import com.nk.blog.enums.BlogPostShortBy;
import com.nk.blog.enums.BlogStatus;
import com.nk.blog.enums.SortOrder;
import com.nk.blog.exception.CustomDataIntegrityViolationException;
import com.nk.blog.exception.DataConflictException;
import com.nk.blog.service.BlogPostService;

@WebMvcTest(BlogPostController.class)
@Import(JacksonConfiguration.class)
class BlogPostControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private BlogPostService mockScheduleService;

        @Autowired
        private ObjectMapper objectMapper;

        private BlogPostListDTO blogPostListDTO;

        private BlogPostDTO blogPostDTO;

        private WebResponse<BlogPostListDTO> webResponseForPagedResponse;

        @BeforeEach
        void setup() {
                blogPostDTO = BlogPostDTO.builder()
                                .id(1L)
                                .title("title")
                                .content("content")
                                .description("description")
                                .imageUrl("imageUrl")
                                .blogStatus(BlogStatus.DRAFT)
                                .createdBy(0L)
                                .createdAt(Testconstants.DEFAULT_DATETIME)
                                .lastUpdatedBy(0L)
                                .lastUpdatedAt(Testconstants.DEFAULT_DATETIME)
                                .build();
                blogPostListDTO = BlogPostListDTO.builder()
                                .blogPostList(List.of(blogPostDTO))
                                .totalPages(2)
                                .sortBy(BlogPostShortBy.CREATED_AT)
                                .sortOrder(SortOrder.ASC).build();
                webResponseForPagedResponse = WebResponse.<BlogPostListDTO>builder().data(blogPostListDTO).build();

        }

        /*
         * ! ==========> POST /api/v1/blogs <==========
         */
        @Test
        void testCreateBlog_ValidRequest_ReturnsDTO() throws Exception {
                // Setup
                BlogPostRequest request = BlogPostRequest.builder()
                                .content("content")
                                .description("description")
                                .imageUrl("imageUrl")
                                .title("title")
                                .status(BlogStatus.DRAFT)
                                .build();
                when(mockScheduleService.createBlog(request))
                                .thenReturn(blogPostDTO);
                WebResponse<BlogPostDTO> expectedWebResponse = WebResponse.<BlogPostDTO>builder().data(blogPostDTO)
                                .build();
                // Run the test
                final MockHttpServletResponse response = mockMvc.perform(post("/api/v1/blogs")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andReturn().getResponse();

                // Verify the results
                assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
                assertThat(response.getContentAsString())
                                .isEqualTo(objectMapper.writeValueAsString(expectedWebResponse));
        }

        @Test
        void testCreateBlog_ValidRequest_ReturnsDTO_With_Existing_Title() throws Exception {
                // Setup
                BlogPostRequest request = BlogPostRequest.builder()
                                .content("content")
                                .description("description")
                                .imageUrl("imageUrl")
                                .title("title")
                                .status(BlogStatus.DRAFT)
                                .build();
                when(mockScheduleService.createBlog(request))
                                .thenThrow(new DataConflictException("Blog already exists with this exact title"));
                WebResponse<BlogPostDTO> expectedWebResponse = WebResponse.<BlogPostDTO>builder()
                                .message("Blog already exists with this exact title").build();
                // Run the test
                final MockHttpServletResponse response = mockMvc.perform(post("/api/v1/blogs")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andReturn().getResponse();

                // Verify the results
                assertThat(response.getStatus()).isEqualTo(HttpStatus.CONFLICT.value());
                assertThat(response.getContentAsString())
                                .isEqualTo(objectMapper.writeValueAsString(expectedWebResponse));
        }

        @Test
        void testCreateBlog_InValidRequest_ReturnsDTO_With_null_Title() throws Exception {
                // Setup
                BlogPostRequest request = BlogPostRequest.builder()
                                .content("content")
                                .description("description")
                                .imageUrl("imageUrl")
                                .status(BlogStatus.DRAFT)
                                .build();
                when(mockScheduleService.createBlog(request))
                                .thenThrow(new CustomDataIntegrityViolationException(
                                                "Failed to create blog due to data integrity violation: "));
                WebResponse<BlogPostDTO> expectedWebResponse = WebResponse.<BlogPostDTO>builder()
                                .message("Failed to create blog due to data integrity violation: ").build();
                // Run the test
                final MockHttpServletResponse response = mockMvc.perform(post("/api/v1/blogs")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andReturn().getResponse();

                // Verify the results
                assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
                assertThat(response.getContentAsString())
                                .isEqualTo(objectMapper.writeValueAsString(expectedWebResponse));
        }

        /*
         * ! ==========> GET /api/v1/blogs <==========
         */
        @Test
        void testGetAllBlogs() throws Exception {

                when(mockScheduleService.getAllBlogs(anyInt(), anyInt(), any(SortOrder.class),
                                any(BlogPostShortBy.class)))
                                .thenReturn(blogPostListDTO);

                final MockHttpServletResponse response = mockMvc
                                .perform(get("/api/v1/blogs?page=1&page_size=10&sort=ASC&sort_by=CREATED_AT")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON))
                                .andReturn().getResponse();

                verify(mockScheduleService, times(1)).getAllBlogs(1, 10, SortOrder.ASC, BlogPostShortBy.CREATED_AT);
                // Verify the results
                assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                assertThat(response.getContentAsString())
                                .isEqualTo(objectMapper.writeValueAsString(webResponseForPagedResponse));
        }

        /*
         * ! ==========> GET /api/v1/blogs/user <==========
         */
        @Test
        void testGetAllBlogsUser() throws Exception {

                when(mockScheduleService.getAllBlogsCurrentUser(anyInt(), anyInt(), any(SortOrder.class),
                                any(BlogPostShortBy.class)))
                                .thenReturn(blogPostListDTO);

                final MockHttpServletResponse response = mockMvc
                                .perform(get("/api/v1/blogs/user?page=1&page_size=10&sort=ASC&sort_by=CREATED_AT")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON))
                                .andReturn().getResponse();

                verify(mockScheduleService, times(1)).getAllBlogsCurrentUser(1, 10, SortOrder.ASC,
                                BlogPostShortBy.CREATED_AT);
                // Verify the results
                assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                assertThat(response.getContentAsString())
                                .isEqualTo(objectMapper.writeValueAsString(webResponseForPagedResponse));
        }

        /*
         * ! ==========> GET /api/v1/blogs/{id} <==========
         */
        @Test
        void testGetBlogById() throws Exception {
                WebResponse<BlogPostDTO> expectedWebResponse = WebResponse.<BlogPostDTO>builder().data(blogPostDTO)
                                .build();

                when(mockScheduleService.getBlogById(anyLong())).thenReturn(blogPostDTO);

                final MockHttpServletResponse response = mockMvc.perform(get("/api/v1/blogs/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andReturn().getResponse();

                verify(mockScheduleService, times(1)).getBlogById(anyLong());
                // Verify the results
                assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                assertThat(response.getContentAsString())
                                .isEqualTo(objectMapper.writeValueAsString(expectedWebResponse));
        }

        /*
         * ! ==========> PUT /api/v1/blogs <==========
         */
        @Test
        void testUpdateBlog() throws Exception {
                // Setup
                BlogPostRequest request = BlogPostRequest.builder()
                                .content("content")
                                .description("description")
                                .imageUrl("imageUrl")
                                .title("title")
                                .status(BlogStatus.DRAFT)
                                .id(1L)
                                .build();
                when(mockScheduleService.updateBlog(1L, request))
                                .thenReturn(blogPostDTO);
                WebResponse<BlogPostDTO> expectedWebResponse = WebResponse.<BlogPostDTO>builder().data(blogPostDTO)
                                .build();
                // Run the test
                final MockHttpServletResponse response = mockMvc.perform(put("/api/v1/blogs/1")
                                .content(objectMapper.writeValueAsString(request))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                                .andReturn().getResponse();

                // Verify the results
                assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                assertThat(response.getContentAsString())
                                .isEqualTo(objectMapper.writeValueAsString(expectedWebResponse));
        }

        /*
         * ! ==========> DELETE /api/v1/blogs/{id} <==========
         */
        @Test
        void testDeleteBlog() throws Exception {
                // Setup
                WebResponse<Boolean> expectedWebResponse = WebResponse.<Boolean>builder().data(true).build();
                // Run the test
                final MockHttpServletResponse response = mockMvc.perform(delete("/api/v1/blogs/1"))
                                .andReturn().getResponse();

                verify(mockScheduleService, times(1)).deleteBlog(anyLong());
                // Verify the results
                assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
                assertThat(response.getContentAsString())
                                .isEqualTo(objectMapper.writeValueAsString(expectedWebResponse));
        }

}
