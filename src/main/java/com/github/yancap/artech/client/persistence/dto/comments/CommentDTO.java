package com.github.yancap.artech.client.persistence.dto.comments;

import com.github.yancap.artech.client.persistence.models.Comment;

public record CommentDTO(
        Long id,
        String text,
        String createdAt,
        String userName,
        String userEmail
) {

    public CommentDTO(Comment comment){
        this(
                comment.getId(),
                comment.getText(),
                comment.getCreatedAt().toString(),
                comment.getUser().getName(),
                comment.getUser().getEmail()
        );
    }
}
