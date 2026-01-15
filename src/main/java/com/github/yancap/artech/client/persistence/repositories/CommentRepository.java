package com.github.yancap.artech.client.persistence.repositories;

import com.github.yancap.artech.client.persistence.models.Article;
import com.github.yancap.artech.client.persistence.models.Comment;
import com.github.yancap.artech.client.persistence.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class CommentRepository implements PanacheRepository<Comment> {

    public Comment findByUserAndCommentId(Long commentId, String userEmail){
        return find(
                "id = ?1 AND user.email = ?2", //"SELECT distinct a FROM Article WHERE a.state LIKE ?1 AND a.manager_id LIKE ?2"
                commentId,
                userEmail
        )
                .firstResultOptional()
                .get();
    }
}
