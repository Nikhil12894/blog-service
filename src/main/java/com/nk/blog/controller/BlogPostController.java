package com.nk.blog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nk.blog.constants.CommonsConstants;
import com.nk.blog.dto.BlogPostDTO;
import com.nk.blog.dto.BlogPostListDTO;
import com.nk.blog.dto.BlogPostRequest;
import com.nk.blog.dto.WebResponse;
import com.nk.blog.enums.BlogPostShortBy;
import com.nk.blog.enums.SortOrder;
import com.nk.blog.service.BlogPostService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogPostController {

    private final BlogPostService blogService;

    @Operation(summary = "create blog post", description = "create blog post", tags = { "Blog Post" })
    @PostMapping
    public ResponseEntity<WebResponse<BlogPostDTO>> createBlog(@RequestBody BlogPostRequest blog) {
        BlogPostDTO createdBlog = blogService.createBlog(blog);
        WebResponse<BlogPostDTO> response = WebResponse.<BlogPostDTO>builder().data(createdBlog).build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "get all blog posts", description = "get all blog posts", tags = { "Blog Post" })
    @Parameter(name = CommonsConstants.PAGE, description = "page number", in = ParameterIn.QUERY)
    @Parameter(name = CommonsConstants.PAGE_SIZE, description = "number of elements per page", in = ParameterIn.QUERY)
    @Parameter(name = CommonsConstants.SORT_ORDER, description = "Order to sort in", in = ParameterIn.QUERY, example = CommonsConstants.DEFAULT_SORT_ORDER)
    @Parameter(name = CommonsConstants.SORT_BY, description = "value to sort by", in = ParameterIn.QUERY, example = CommonsConstants.DEFAULT_SORT_CREATED)
    @GetMapping
    public ResponseEntity<WebResponse<BlogPostListDTO>> getAllBlogs(
            @RequestParam(value = CommonsConstants.PAGE, required = false) Integer page,
            @RequestParam(value = CommonsConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = CommonsConstants.SORT_ORDER, defaultValue = CommonsConstants.DEFAULT_SORT_ORDER) SortOrder sort,
            @RequestParam(value = CommonsConstants.SORT_BY, defaultValue = CommonsConstants.DEFAULT_SORT_CREATED ) BlogPostShortBy sortBy
    ) {
        BlogPostListDTO blogs = blogService.getAllBlogs(page, pageSize, sort, sortBy);
        WebResponse<BlogPostListDTO> response = WebResponse.<BlogPostListDTO>builder().data(blogs).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    
    @Operation(summary = "get all blog posts for current user", description = "get all blog posts for current user", tags = { "Blog Post" })
    @Parameter(name = CommonsConstants.PAGE, description = "page number", in = ParameterIn.QUERY)
    @Parameter(name = CommonsConstants.PAGE_SIZE, description = "number of elements per page", in = ParameterIn.QUERY)
    @Parameter(name = CommonsConstants.SORT_ORDER, description = "Order to sort in", in = ParameterIn.QUERY, example = CommonsConstants.DEFAULT_SORT_ORDER)
    @Parameter(name = CommonsConstants.SORT_BY, description = "value to sort by", in = ParameterIn.QUERY, example = CommonsConstants.DEFAULT_SORT_CREATED)
    @GetMapping("/user")
    public ResponseEntity<WebResponse<BlogPostListDTO>> getAllBlogsCurrentUser(
            @RequestParam(value = CommonsConstants.PAGE, required = false) Integer page,
            @RequestParam(value = CommonsConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = CommonsConstants.SORT_ORDER, defaultValue = CommonsConstants.DEFAULT_SORT_ORDER) SortOrder sort,
            @RequestParam(value = CommonsConstants.SORT_BY, defaultValue = CommonsConstants.DEFAULT_SORT_CREATED ) BlogPostShortBy sortBy
    ) {
        BlogPostListDTO blogs = blogService.getAllBlogsCurrentUser(page, pageSize, sort, sortBy);
        WebResponse<BlogPostListDTO> response = WebResponse.<BlogPostListDTO>builder().data(blogs).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "get blog post by id", description = "get blog post by id", tags = { "Blog Post" })
    @Parameter(name = "id", description = "blog post id", in = ParameterIn.PATH)
    @GetMapping("/{id}")
    public ResponseEntity<WebResponse<BlogPostDTO>> getBlogById(@PathVariable Long id) {
        BlogPostDTO blog = blogService.getBlogById(id);
        WebResponse<BlogPostDTO> response = WebResponse.<BlogPostDTO>builder().data(blog).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "update blog post", description = "update blog post", tags = { "Blog Post" })
    @Parameter(name = "id", description = "blog post id", in = ParameterIn.PATH)
    @PutMapping("/{id}")
    public ResponseEntity<WebResponse<BlogPostDTO>> updateBlog(@PathVariable Long id, @RequestBody BlogPostRequest blog) {
        BlogPostDTO updatedBlog = blogService.updateBlog(id, blog);
        WebResponse<BlogPostDTO> response = WebResponse.<BlogPostDTO>builder().data(updatedBlog).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "delete blog post", description = "delete blog post", tags = { "Blog Post" })
    @Parameter(name = "id", description = "blog post id", in = ParameterIn.PATH)
    @DeleteMapping("/{id}")
    public ResponseEntity<WebResponse<Boolean>> deleteBlog(@PathVariable Long id) {
        blogService.deleteBlog(id);
        WebResponse<Boolean> response = WebResponse.<Boolean>builder().data(true).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
    
}
