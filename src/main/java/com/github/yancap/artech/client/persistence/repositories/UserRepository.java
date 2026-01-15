package com.github.yancap.artech.client.persistence.repositories;
import com.github.yancap.artech.client.persistence.models.Management;
import com.github.yancap.artech.client.persistence.models.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User>{
    public User findByEmail(String email) {
        return find("email", email).firstResult();
    }



}
