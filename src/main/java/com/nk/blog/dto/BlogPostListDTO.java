package com.nk.blog.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nk.blog.enums.BlogPostShortBy;
import com.nk.blog.enums.SortOrder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
* Model for paged blog post result.
*/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogPostListDTO {
    private List<BlogPostDTO> blogPostList;
	@JsonProperty("total_pages")
	private Integer totalPages;
	private Long total;
	@JsonProperty("sort_order")
	private SortOrder sortOrder;
	@JsonProperty("sort_by")
	private BlogPostShortBy sortBy;
}
