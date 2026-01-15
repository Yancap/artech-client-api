package com.github.yancap.artech.client.services;

import com.github.yancap.artech.client.utils.enums.TypeError;
import com.github.yancap.artech.client.utils.errors.ArtechError;
import com.github.yancap.artech.client.utils.errors.ArtechException;
import com.github.yancap.artech.client.persistence.dto.comments.CreateCommentRequestDTO;
import com.github.yancap.artech.client.persistence.models.Comment;
import com.github.yancap.artech.client.persistence.repositories.ArticleRepository;
import com.github.yancap.artech.client.persistence.repositories.CommentRepository;
import com.github.yancap.artech.client.persistence.repositories.UserRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Date;

@ApplicationScoped
public class CommentService {


    @Inject
    private CommentRepository commentRepository;

    @Inject
    private ArticleRepository articleRepository;

    @Inject
    private UserRepository userRepository;

    public void create(CreateCommentRequestDTO dto, String userEmail){
        var user = userRepository.findByEmail(userEmail);
        if (user == null) {
            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("O usuário não existe.");
            error.setDetails("Verifique se o usuário atual existe ou entre em contato com o suporte.");
            error.setStatus(404);
            throw new ArtechException(error);
        }
        var article = articleRepository.findBySlug(dto.articleSlug());
        if (article == null) {
            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("O artigo não existe.");
            error.setDetails("Verifique se o artigo atual existe ou se foi excluido.");
            error.setStatus(404);
            throw new ArtechException(error);
        }
        var commentCreated = new Comment();
        commentCreated.setCreatedAt(new Date());
        commentCreated.setText(dto.text());
        commentCreated.setUser(user);
        commentCreated.setArticle(article);
        commentRepository.persist(commentCreated);
    }

    public void deleteById(Long id, String userEmail){
        try {
            commentRepository.findByUserAndCommentId(id, userEmail);
            commentRepository.deleteById(id);
        } catch (Throwable t) {
            var error = new ArtechError();
            error.setType(TypeError.ERROR);
            error.setMessage("Não foi possivel deletar o comentário.");
            error.setDetails("Verifique se o comentário é seu ou entre em contato com o suporte.");
            error.setStatus(404);
            throw new ArtechException(error);

        }
    }

}
