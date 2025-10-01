package com.github.yancap.artech.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "credits")
@Data
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String link;

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "article_id")
    private Article article;
}
