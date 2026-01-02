package com.github.yancap.artech.persistence.repositories;


import com.github.yancap.artech.persistence.models.Article;
import com.github.yancap.artech.persistence.models.ArticleTags;
import com.github.yancap.artech.persistence.models.Tag;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TagRepository implements PanacheRepository<Tag> {

    @Inject
    private ArticleTagsRepository articleTagsRepository;



    public Tag persistIfNotExist(String tag) {
        var currentTag = find("tag", tag.toLowerCase()).firstResult();
        if(currentTag == null) {
            Tag tagEntity = new Tag();
            tagEntity.setTag(tag);

            persist(tagEntity);
            //setCurrentArticleToTag(tagEntity, article);
            return tagEntity;
        }
        return currentTag;
    }
    private void setCurrentArticleToTag(Tag tag, Article article) {
        var articleTags = new ArticleTags();
        articleTags.setArticleId(article.getId());
        articleTags.setTagId(tag.getId());
        articleTagsRepository.persist(articleTags);
    }

    public List<Article> getArticles(Tag tag){
        var articleTagsRepository = new ArticleTagsRepository();
        return articleTagsRepository.getArticlesByTag(tag);
    }
}
