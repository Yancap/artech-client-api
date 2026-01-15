package com.github.yancap.artech.client.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity(name = "ClientComment")
@Table(name = "comments")
@Data
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String text;

    @Column(name = "created_at")
    private Date createdAt = new Date();

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "user_id")
    private User user;

}


