package com.github.yancap.artech.persistence.repositories;


import com.github.yancap.artech.persistence.dto.article.ArticleEntityDTO;
import com.github.yancap.artech.persistence.dto.category.CategoryEntityDTO;
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

    public List<ArticleEntityDTO> queryEngine(String queryParam) {

        var slicedQueryParam = queryParam.split(" ");
        var categoryQuery = "category LIKE ";
        for (int i = 0; i < slicedQueryParam.length; i++){
            categoryQuery = categoryQuery + "'%" + slicedQueryParam[i] + "%'";
            if (i != (slicedQueryParam.length - 1)) {
                categoryQuery = categoryQuery + " OR category LIKE ";
            }
        }
        var result = getEntityManager()
                .createNativeQuery("SELECT * FROM categories WHERE  "
                        + categoryQuery + ";", CategoryEntityDTO.class
                )
                .getResultList();
        List<List<ArticleEntityDTO>> listofArticleList = result.stream()
                .map((Object object) -> {
                    var categoryEntity = (CategoryEntityDTO) object;
                    List<ArticleEntityDTO> articleList = new ArrayList<>();
                    for (ArticleEntityDTO article : categoryEntity.getArticles()) {
                        articleList.add(article);
                    }
                    return articleList;

                }).toList();
        List<ArticleEntityDTO> articleFilteredFromQueryCategory = new ArrayList<>();
        for(List<ArticleEntityDTO> articleList: listofArticleList) {
            for(ArticleEntityDTO article: articleList) {
                articleFilteredFromQueryCategory.add(article);
            }
        }

        return articleFilteredFromQueryCategory;
    }


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
