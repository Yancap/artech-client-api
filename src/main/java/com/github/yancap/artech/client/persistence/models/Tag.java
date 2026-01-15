package com.github.yancap.artech.client.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "ClientTag")
@Table(name = "tags")
@Data
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String tag;


//    public static void setArticles(Tag tag, List<Article> articles) {
//        var articleTagsList = articles.stream().map((article) -> {
//            var articleTags = new ArticleTags();
//            articleTags.setArticle(article);
//            articleTags.setTag(tag);
//            return articleTags;
//        }).toList();
////        var articleTagsRepository = ArticleTagsRepository.INSTANCE;
//        var articleTagsRepository = new ArticleTagsRepository();
//        articleTagsRepository.persist(articleTagsList);
//    }

}
