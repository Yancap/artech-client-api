package com.github.yancap.artech.client.persistence.dto.comments;

import com.github.yancap.artech.client.persistence.dto.article.ArticleDTO;

import java.util.List;

public record ArticleCommentResponseDTO(
        String articleTitle,
        String articleSlug,
        List<CommentDTO> commentsList
) {
    public ArticleCommentResponseDTO(ArticleDTO articleDTO) {
        this(
                articleDTO.title(),
                articleDTO.slug(),
                articleDTO.comments()
        );
    }
}
