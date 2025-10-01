package com.github.yancap.artech.persistence.dto.article;

import com.github.yancap.artech.persistence.models.Category;
import com.github.yancap.artech.persistence.models.Management;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "articles")
@Data
public class ArticleEntityDTO {
    @Id
    @Column(name = "id")
    private Long id;

    @Column
    private String slug;

    @Column
    private String title;

    @Column
    private String subtitle;

    @Column
    private String text;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @Column(name = "updated_at")
    private Date updatedAt = new Date();

    @Column(name = "current_state")
    private String currentState;

    @Column(name = "manager_id")
    private Long managerId;

    @Column(name = "category_id")
    private Long categoryId;




}
