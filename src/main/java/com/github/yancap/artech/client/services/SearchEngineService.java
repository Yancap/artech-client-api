package com.github.yancap.artech.client.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.github.yancap.artech.client.persistence.dto.article.ArticleEntityDTO;
import com.github.yancap.artech.client.persistence.repositories.ArticleRepository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class SearchEngineService {

    @Inject
    ArticleRepository articleRepository;


    @ConfigProperty(name = "application.file.csv.trash-words.path")
    String csvFilePath;

    public List<ArticleEntityDTO> searchArticle(String query) {
        var cleanQuery = filterTrashWordFromQuery(query).toLowerCase();
        return articleRepository.queryEngine(cleanQuery);
    }


    public List<ArticleEntityDTO> searchArticleByTag(String hashtags) {
        var cleanQuery = filterTrashWordFromQuery(hashtags).toLowerCase();
        return articleRepository.queryEngineByTag(cleanQuery);
    }


    private String filterTrashWordFromQuery(String query) {
        var wordQuery = query.split(" ");
        var trashWords = Arrays.asList(getCsvTrashWords());

        var cleanQuery = "";

        for (String queryW : wordQuery) {
            var isTrashWord = trashWords.contains(queryW); //|| trashPunctuation.contains(queryW);
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
