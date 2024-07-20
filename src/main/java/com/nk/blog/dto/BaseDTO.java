package com.nk.blog.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public class BaseDTO {
    @JsonProperty("created_by")
    private Long createdBy;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("last_updated_by")
    private Long lastUpdatedBy;

    @JsonProperty("last_updated_at")
    private LocalDateTime lastUpdatedAt;
}
