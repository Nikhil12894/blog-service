package com.nk.blog.model;

import com.nk.blog.enums.BlogStatus;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
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
@Table(name = "blog_post")
@Entity
public class BlogPost extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false, length = 500)
    private String description;
    @Column(name = "content", nullable = false)
    @Lob @Basic(fetch = jakarta.persistence.FetchType.LAZY) // Large Object
    private String content;
    @Column(name = "image_url")
    private String imageUrl;
    @Column(name = "status")
    @Enumerated(value = jakarta.persistence.EnumType.STRING)
    private BlogStatus status;

}
