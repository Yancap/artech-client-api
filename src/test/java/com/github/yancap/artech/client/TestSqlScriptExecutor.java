package com.github.yancap.artech.client;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class TestSqlScriptExecutor {

    @Inject
    EntityManager entityManager;

    public void start(){
        entityManager
                .createNativeQuery("RUNSCRIPT FROM 'classpath:script/charge_database.sql'")
                .executeUpdate();
    }
}
