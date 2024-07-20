package com.nk.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BlogPostController.class)
public class BlogPostController {
     @Autowired
    private MockMvc mockMvc;
    
}
