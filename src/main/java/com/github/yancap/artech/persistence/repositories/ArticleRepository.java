package com.github.yancap.artech.persistence.repositories;

import java.util.List;

import com.github.yancap.artech.persistence.dto.article.ArticleEntityDTO;
import com.github.yancap.artech.persistence.models.Article;
import com.github.yancap.artech.persistence.models.Category;
import com.github.yancap.artech.utils.enums.ArticleStates;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ArticleRepository implements PanacheRepository<Article> {

    public List<ArticleEntityDTO> queryEngine(String queryParam) {

        var slicedQueryParam = queryParam.split("[\\s\\d\\W]+");
        var titleQuery = "LOWER(a.title) LIKE ";
        var subtitleQuery = "LOWER(a.subtitle) LIKE ";
        var textQuery = "LOWER(a.text) LIKE ";
        var tagQuery = "tag LIKE ";
        var categoryQuery = "category LIKE ";
        for (int i = 0; i < slicedQueryParam.length; i++){
            titleQuery = titleQuery + "'%" + slicedQueryParam[i] + "%'";
            subtitleQuery = subtitleQuery + "'%" + slicedQueryParam[i] + "%'";
            textQuery = textQuery + "'%" + slicedQueryParam[i] + "%'";
            tagQuery = tagQuery + "'%" + slicedQueryParam[i] + "%'";
            categoryQuery = categoryQuery + "'%" + slicedQueryParam[i] + "%'";
            if (i != (slicedQueryParam.length - 1)) {
                titleQuery = titleQuery + " OR LOWER(a.title) LIKE ";
                subtitleQuery = subtitleQuery + " OR LOWER(a.subtitle) LIKE ";
                textQuery = textQuery + " OR LOWER(a.text) LIKE ";
                tagQuery = tagQuery + " OR tag LIKE ";
                categoryQuery = categoryQuery + " AND category LIKE ";
            }
        }

        
        var nativeQuery = "SELECT a.*, m.name FROM articles AS a, managements AS m"
        + " WHERE a.current_state = 'active' AND (" + titleQuery +
                " OR " + subtitleQuery +
                " OR " + textQuery +
            " OR a.id IN (" +
                "SELECT article_id FROM articles_tags WHERE tag_id IN (" +
                    "select id from tags where " + tagQuery +
                    ")" +
                ")" +
            " OR a.category_id IN (" +
                "SELECT id FROM categories WHERE " + categoryQuery +
            ")) AND a.manager_id = m.id;" ;
        List<ArticleEntityDTO> result = getEntityManager()
            .createNativeQuery(nativeQuery, ArticleEntityDTO.class)
            .getResultList();
        
        return result;
    }

    public List<ArticleEntityDTO> queryEngineByTag(String tag) {

        //Split de caracteres especiais
        var slicedQueryParam = tag.split("[\\s\\d\\W]+");
        var tagQuery = "tag = ";
        for (int i = 0; i < slicedQueryParam.length; i++){
            tagQuery = tagQuery + "'" + slicedQueryParam[i] + "'";
            if (i != (slicedQueryParam.length - 1)) {
                tagQuery = tagQuery + " OR tag = ";
            }
        }


        var nativeQuery = "SELECT a.*, m.name FROM articles AS a, managements AS m WHERE a.current_state = 'active' AND" +
                " a.id IN (" +
                    "SELECT article_id FROM articles_tags WHERE tag_id IN (" +
                        "select id from tags where " + tagQuery +
                    ")" +
                ") AND a.manager_id = m.id;" ;
        List<ArticleEntityDTO> result = getEntityManager()
                .createNativeQuery(nativeQuery, ArticleEntityDTO.class)
                .getResultList();
        return (List<ArticleEntityDTO>) result;
    }


    public List<Article> listAllActive() {
        return find("currentState", ArticleStates.active.name()).list();
    }

    public List<Article> findByCategory(Category category) {
        return
                find(
                        "currentState = ?1 and category = ?2",
                        ArticleStates.active.name(),
                        category
                ).list();
    }

    public Article findBySlug(String slug) {
        return find("slug", slug).firstResult();
    }

    public List<Article> findAuthorArticlesByState(String state, Long managerId){
        return find(
            "currentState = ?1 AND manager.id = ?2", //"SELECT distinct a FROM Article WHERE a.state LIKE ?1 AND a.manager_id LIKE ?2"
            state,
            managerId
        ).list();
    }

    public List<Article> findArticlesByManagerId(Long managerId){
        return find(
                "manager.id = ?1",
                managerId
        ).list();
    }

    public void deleteBySlug(String slug) {
        Article article = find("slug", slug).firstResult();
        delete(article);
    }

}
