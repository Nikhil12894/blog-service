package com.nk.blog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nk.blog.enums.BlogStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogPostDTO extends BaseDTO {
    @JsonProperty("blog_id")
    private Long id;
    @JsonProperty("blog_title")
    private String title;
    @JsonProperty("blog_description")
    private String description;
    @JsonProperty("blog_content")
    private String content;
    @JsonProperty("blog_image_url")
    private String imageUrl;
    @JsonProperty("blog_status")
    private BlogStatus blogStatus;
}
