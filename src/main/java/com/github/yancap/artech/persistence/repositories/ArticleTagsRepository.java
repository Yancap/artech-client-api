package com.github.yancap.artech.persistence.repositories;


import com.github.yancap.artech.persistence.models.Article;
import com.github.yancap.artech.persistence.models.ArticleTags;
import com.github.yancap.artech.persistence.models.Tag;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class ArticleTagsRepository implements PanacheRepository<ArticleTags> {

    @Inject
    public ArticleRepository articleRepository;

    @Inject
    public TagRepository tagRepository;



    public List<Article> getArticlesByTag(Tag tag) {
        var articleTagsList = find("tagId", tag.getId()).list();
        Set<Long> articlesIdSet = articleTagsList
                .stream()
                .map(ArticleTags::getArticleId)
                .collect(Collectors.toSet());
        var articles = articlesIdSet.stream().map(id -> articleRepository.findById(id));
        return articles.toList();
    }

    public List<Tag> getTagsByArticle(Article article) {
        var articleTagsList = find("articleId", article.getId()).list();
        Set<Long> tagsIdSet = articleTagsList
                .stream()
                .map(ArticleTags::getTagId)
                .collect(Collectors.toSet());
        var tags = tagsIdSet.stream().map(id -> tagRepository.findById(id));
        return tags.toList();
    }

    public void persistCurrentArticleToTag(Tag tag, Article article) {
        var articleTags = new ArticleTags();
        articleTags.setArticleId(article.getId());
        articleTags.setTagId(tag.getId());
        persist(articleTags);
    }

    public void removeRelationshipByArticle(Article article) {
        var articleTagsList = list("articleId", article.getId());
        articleTagsList.forEach(this::delete);
    }

}
