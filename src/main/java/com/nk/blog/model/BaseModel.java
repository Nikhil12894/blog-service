package com.nk.blog.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@MappedSuperclass
public class BaseModel {
    // userid: userid: blog_user id of the user who created this record
    @Column(name="created_by")
    private Long createdBy;

    // datetime when this record was created
    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;

    // userid: userid: blog_user id of the user who updated this record
    @Column(name="last_updated_by")
    private Long lastUpdatedBy;

    // datetime when this record was updated
    @LastModifiedDate
    @Column(name="last_updated_at")
    private LocalDateTime lastUpdatedAt;
}
