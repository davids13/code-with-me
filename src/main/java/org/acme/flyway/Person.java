package org.acme.flyway;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
public class Person extends PanacheEntity {
     String name;
     String email;
     LocalDateTime registeredAt;

    public Person() {
    }

    public Person(String name, String email, LocalDateTime registeredAt) {
        this.name = name;
        this.email = email;
        this.registeredAt = registeredAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }
}
