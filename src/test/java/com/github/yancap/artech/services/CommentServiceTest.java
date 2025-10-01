package com.github.yancap.artech.services;

import com.github.yancap.artech.TestSqlScriptExecutor;
import com.github.yancap.artech.persistence.dto.comments.CreateCommentRequestDTO;
import com.github.yancap.artech.persistence.repositories.CommentRepository;
import com.github.yancap.artech.utils.errors.ArtechException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@QuarkusTest
public class CommentServiceTest {

    @Inject
    CommentService service;

    @Inject
    CommentRepository repository;

    @Inject
    TestSqlScriptExecutor sqlScript;

    @BeforeEach
    @Transactional
    void setup(){
        sqlScript.start();
    }

    @Test
    @Transactional
    @DisplayName("it should be able to create comment")
    void createComment() {
        CreateCommentRequestDTO dto = new CreateCommentRequestDTO(
                "article-example-1",
                "Comentario 1"
        );
        service.create(dto, "usuario@email.com");
        var comment = repository.find("text", "Comentario 1");
        assertNotNull(comment);
    }


    @Test
    @Transactional
    @DisplayName("it should not be able to create comment if user not exist")
    void dontCreateCommentIfUserNotExist() {
        CreateCommentRequestDTO dto = new CreateCommentRequestDTO(
                "article-example-1",
                "Comentario 1"
        );
        assertThrows(ArtechException.class,
                () -> service.create(dto, "notExist@email.com")
        );
    }
    @Test
    @Transactional
    @DisplayName("it should not be able to create comment if article not exist")
    void dontCreateCommentIfArticleNotExist() {
        CreateCommentRequestDTO dto = new CreateCommentRequestDTO(
                "not-exist-example-1",
                "Comentario 1"
        );
        assertThrows(ArtechException.class,
                () -> service.create(dto, "usuario@email.com")
        );
    }

    @Test
    @Transactional
    @DisplayName("it should be able to delete comment by id")
    void deleteCommentById(){
        var comment = repository.findById(100L);
        assertNotNull(comment);

        service.deleteById(100L, "usuario@email.com");

        comment = repository.findById(100L);
        assertNull(comment);
    }

    @Test
    @Transactional
    @DisplayName("it should not be able to delete comment if not exist")
    void dontDeleteCommentIfNotExist(){
        assertThrows(ArtechException.class,
                () -> service.deleteById(999L, "usuario@email.com")
        ) ;

    }

}
