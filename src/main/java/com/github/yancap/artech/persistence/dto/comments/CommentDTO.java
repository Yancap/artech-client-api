package com.github.yancap.artech.persistence.dto.comments;

import com.github.yancap.artech.persistence.models.Comment;

public record CommentDTO(
        Long id,
        String text,
        String createdAt,
        String userName
) {

    public CommentDTO(Comment comment){
        this(
                comment.getId(),
                comment.getText(),
                comment.getCreatedAt().toString(),
                comment.getUser().getName()
        );
    }
}
