package com.github.yancap.artech.client.persistence.repositories;

import com.github.yancap.artech.client.persistence.models.Credit;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class CreditRepository implements PanacheRepository<Credit> {
    public Credit persistIfNotExist(Credit credit) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", credit.getName());
        params.put("link", credit.getLink());
        params.put("article", credit.getArticle());
        var currentTag = find("name = :name and link = :link and article = :article", params).firstResult();
        if(currentTag == null) {
            persist(credit);
            return credit;
        }
        return currentTag;
    }
}
