package com.nk.blog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.nk.blog.constants.CommonsConstants;
import com.nk.blog.enums.BlogStatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request object for the api
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(
  description = "Request object for Blog Post",
  requiredMode = Schema.RequiredMode.REQUIRED
)
public class BlogPostRequest {

  @JsonProperty("blog_id")
  @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "ID of the blog post (used for updating)")
  private Long id;

  @JsonProperty("blog_title")
  @NotBlank(message = "Blog title cannot be blank")
  @Schema(
    requiredMode = Schema.RequiredMode.REQUIRED,
    description = "Title of the blog post"
  )
  private String title;

  @NotBlank(message = "Blog description cannot be blank")
  @JsonProperty("blog_description")
  @Schema(
    requiredMode = Schema.RequiredMode.REQUIRED,
    description = "Description of the blog post"
  )
  private String description;

  @NotBlank(message = "Blog content cannot be blank")
  @JsonProperty("blog_content")
  @Schema(
    requiredMode = Schema.RequiredMode.REQUIRED,
    description = "Content of the blog post"
  )
  private String content;

  @JsonProperty("blog_image_url")
  @Schema(
    requiredMode = Schema.RequiredMode.NOT_REQUIRED,
    description = "Image URL of the blog post"
  )
  private String imageUrl;

  @NotBlank(message = "Blog status cannot be blank")
  @JsonProperty("blog_status")
  @Schema(
    requiredMode = Schema.RequiredMode.NOT_REQUIRED,
    description = "Status of the blog post",
    allowableValues = CommonsConstants.ALLOWED_BLOG_STATUS,
    example = "DRAFT",
    defaultValue = CommonsConstants.DEFAULT_BLOG_STATUS
  )
  private BlogStatus status;
}
