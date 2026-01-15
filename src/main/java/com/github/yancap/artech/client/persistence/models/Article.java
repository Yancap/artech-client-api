package com.github.yancap.artech.client.persistence.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

import com.github.yancap.artech.client.utils.enums.ArticleStates;

@Entity(name = "ClientArticle")
@Table(name = "articles")
@Data
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne(cascade = { CascadeType.MERGE })
    @JoinColumn(name = "manager_id")
    private Management manager;


    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(targetEntity=Credit.class, mappedBy = "article", cascade = { CascadeType.ALL })
    private List<Credit> credits;

    @OneToMany(targetEntity=Comment.class, mappedBy = "article", cascade = { CascadeType.ALL })
    private List<Comment> comments;

//    public static List<Tag> getTags(Article article){
//        var articleTagsRepository = ArticleTagsRepository.INSTANCE;
//        var articleTagsList = articleTagsRepository.find("article_id", article.getId()).list();
//        return articleTagsList.stream().map(ArticleTags::getTag).toList();
//    }
//
//    public void setTags(List<Tag> tags){
//        var articleTagsList = tags.stream().map((tag) -> {
//            var articleTags = new ArticleTags();
//            articleTags.setArticle(this);
//            articleTags.setTag(tag);
//            return articleTags;
//        }).toList();
//        var articleTagsRepository = ArticleTagsRepository.INSTANCE;
//        articleTagsRepository.persist(articleTagsList);
//        //return new ArrayList<>();
//    }

    @PrePersist
    public void prePersist() {
        var currentState = getCurrentState();
        if(
            getTitle().isEmpty() ||
            getText().isEmpty() ||
            getSubtitle().isEmpty() ||
            getImageUrl().isEmpty()
        ) {
            setCurrentState(ArticleStates.draft.name());
        } else if(!(
            currentState.equals(ArticleStates.active.name()) ||
            currentState.equals(ArticleStates.disabled.name()) ||
            currentState.equals(ArticleStates.draft.name())
        )) {
            setCurrentState(ArticleStates.active.name());
        }
    }
}
