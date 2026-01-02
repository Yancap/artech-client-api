package com.github.yancap.artech.persistence.dto.article;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.SecondaryTable;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "articles")
@SecondaryTable(name="managements")
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

    // @Column(name = "current_state")
    // private String currentState;

    // @Column(name = "manager_id")
    // private Long managerId;

    @Column(name = "name", table="managements")
    private String authorName;

    @Column(name = "category_id")
    private Long categoryId;




}
