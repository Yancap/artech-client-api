package com.github.yancap.artech.persistence.dto.category;

import com.github.yancap.artech.persistence.dto.article.ArticleEntityDTO;
import com.github.yancap.artech.persistence.models.Article;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "categories")
@Data
public class CategoryEntityDTO {

    @Id
    @Column(name = "id")
    private Long id;

    @Column
    private String category;

    @OneToMany(targetEntity= ArticleEntityDTO.class, mappedBy = "categoryId", orphanRemoval = true)
    private List<ArticleEntityDTO> articles;
}
