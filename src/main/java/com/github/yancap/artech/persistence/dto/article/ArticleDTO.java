package com.github.yancap.artech.persistence.dto.article;

import com.github.yancap.artech.persistence.dto.admin.GetAuthorDTO;
import com.github.yancap.artech.persistence.dto.comments.CommentDTO;
import com.github.yancap.artech.persistence.dto.credits.CreditDTO;
import com.github.yancap.artech.persistence.models.Article;
import com.github.yancap.artech.persistence.models.Tag;

import java.util.List;

public record ArticleDTO(
        Long id,
        String slug,
        String title,
        String subtitle,
        String text,
        String imageUrl,
        String createdAt,
        String updatedAt,
        String currentState,
        GetAuthorDTO author,
        String category,
        List<String> tags,
        List<CreditDTO> credits,
        List<CommentDTO> comments
) {
    public ArticleDTO(Article article, List<Tag> tags) {
        this(
                article.getId(),
                article.getSlug(),
                article.getTitle(),
                article.getSubtitle(),
                article.getText(),
                article.getImageUrl(),
                article.getCreatedAt().toString(),
                article.getUpdatedAt().toString(),
                article.getCurrentState(),
                new GetAuthorDTO(
                        article.getManager().getName(),
                        article.getManager().getEmail(),
                        article.getManager().getUrlAvatar(),
                        0
                ),
                article.getCategory().getCategory(),
                tags.stream().map(Tag::getTag).toList(),
                article.getCredits().stream().map(
                    credit -> new CreditDTO(credit.getName(), credit.getLink())
                ).toList(),
                article.getComments().stream().map(
                    CommentDTO::new
                ).toList()
        );
    }
}
