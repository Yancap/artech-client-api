package com.github.yancap.artech.persistence.dto.comments;

public record CreateCommentRequestDTO(
        String articleSlug,
        String text
) {
}
