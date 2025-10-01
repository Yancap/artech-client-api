package com.github.yancap.artech.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "articles_tags")
@Data
public class ArticleTags {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;

    @Id
    @Column(name = "article_id")
    private Long articleId;

    @Id
    @Column(name = "tag_id")
    private Long tagId;

}
