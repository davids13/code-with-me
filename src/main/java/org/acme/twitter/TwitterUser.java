package org.acme.twitter;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
public class TwitterUser extends PanacheEntity {

    @Column(unique = true, nullable = false)
    public String username;
    @Column(nullable = false)
    public String displayName;
    public String bio;
    @Column(nullable = false)
    public LocalDateTime createdAt;
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL)
    public List<Chirp> chirps;

    public static TwitterUser findByUsername(String username) {
        return find("username", username).firstResult();
    }

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
