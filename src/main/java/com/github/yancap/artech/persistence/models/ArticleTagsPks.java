package com.github.yancap.artech.persistence.models;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;


@Data
@Embeddable
public class ArticleTagsPks implements Serializable {


    private Long articleId;

    private Long tagId;
}
