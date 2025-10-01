package com.github.yancap.artech.rest;

import com.github.yancap.artech.persistence.dto.article.ArticleListResponseDTO;
import com.github.yancap.artech.persistence.dto.article.ArticleResponseDTO;
import com.github.yancap.artech.services.ArticleService;
import com.github.yancap.artech.services.SearchEngineService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

@Path("api/v1/article/")
public class ArticleResource {

    @Inject
    JsonWebToken jwt;

    @Inject
    ArticleService articleService;

    @Inject
    SearchEngineService searchEngineService;


    @GET
    public Response getArticles(){
        var articles = articleService.getArticles();
        var response = new ArticleListResponseDTO(articles);
        return Response.ok().entity(response).build();
    }


    @GET
    @Path("search")
    public Response searchArticles(
            @QueryParam("q") String query,
            @QueryParam("hashtag") String hashtag
    ){
        if (hashtag != null) {
            var articles = searchEngineService.searchArticleByTag(hashtag);
            return Response.ok(articles).build();

        } else if (query != null) {
            var articles = searchEngineService.searchArticle(query);
            return Response.ok(articles).build();

        }
        return Response.status(404).build();
    }


    @GET
    @Path("{slug}")
    public Response getArticleBySlug(@PathParam("slug") String slug){
        var article = articleService.getArticleBySlug(slug);

        if (article == null) return Response.status(404).build();

        var response = new ArticleResponseDTO(article);
        return Response.ok().entity(response).build();
    }

    @GET
    @Path("category/{category}")
    public Response getArticlesByCategory(@PathParam("category") String category){
        var articles = articleService.getArticlesByCategory(category);
        var response = new ArticleListResponseDTO(articles);
        return Response.ok().entity(response).build();
    }



}