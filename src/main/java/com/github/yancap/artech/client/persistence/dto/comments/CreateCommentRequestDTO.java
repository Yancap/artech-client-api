package com.github.yancap.artech.client.persistence.dto.comments;

public record CreateCommentRequestDTO(
        String articleSlug,
        String text
) {
}
