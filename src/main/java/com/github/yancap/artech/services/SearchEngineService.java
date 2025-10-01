package com.github.yancap.artech.services;

import com.github.yancap.artech.persistence.dto.article.ArticleEntityDTO;
import com.github.yancap.artech.persistence.dto.category.CategoryEntityDTO;
import com.github.yancap.artech.persistence.models.Article;
import com.github.yancap.artech.persistence.repositories.ArticleRepository;
import com.github.yancap.artech.persistence.repositories.CategoryRepository;
import com.github.yancap.artech.persistence.repositories.TagRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class SearchEngineService {

    @Inject
    ArticleRepository articleRepository;

    @Inject
    CategoryRepository categoryRepository;

    @Inject
    TagRepository tagRepository;

    @ConfigProperty(name = "application.file.csv.trash-words.path")
    String csvFilePath;

    public List searchArticle(String query) {
        var cleanQuery = filterTrashWordFromQuery(query).toLowerCase();
        return articleRepository.queryEngine(cleanQuery);
    }


    public List searchArticleByTag(String hashtag) {
        var cleanQuery = filterTrashWordFromQuery(hashtag).toLowerCase();
        return articleRepository.queryEngineByTag(cleanQuery);
    }


    private String filterTrashWordFromQuery(String query) {
        var wordQuery = query.split(" ");
        var trashWords = getCsvTrashWords();
//        var trashPunctuation = Arrays.stream(",;,:,.,/,|,],},[,{,+,=,-,_,),(,*,&,¨,%,$,#,@,!".split(","))
//                .toList();
//        trashPunctuation.add(",");
        var cleanQuery = "";

        for (String queryW : wordQuery) {
            var isTrashWord = Arrays.asList(trashWords).contains(queryW); //|| trashPunctuation.contains(queryW);
            if (!isTrashWord) {
                cleanQuery = cleanQuery + queryW + " ";
            }

        }
        return cleanQuery;
    };


    private String[] getCsvTrashWords() {

        BufferedReader br = null;
        String line = "";
        String csvDivisor = ",";
        String[] trashWords = {};
        try {
            String currentDir = new File("").getAbsolutePath();
            br = new BufferedReader(new FileReader(currentDir + csvFilePath));
            while ((line = br.readLine()) != null) {
                trashWords = line.split(csvDivisor);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return trashWords;
        }
    }
}
