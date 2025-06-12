package org.acme.secure;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.security.jpa.Password;
import io.quarkus.security.jpa.PasswordType;
import io.quarkus.security.jpa.Roles;
import io.quarkus.security.jpa.UserDefinition;
import io.quarkus.security.jpa.Username;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "test_user")
@UserDefinition // Marks this JPA entity as a security user.
public class User extends PanacheEntity {

    @Username
    public String username;
    @Password(PasswordType.CLEAR)
    public String password;
    @Roles
    public String role;

    public static void add(String username, String password, String role) {
        User user = new User();
        user.username = username;
        // For testing I am using clear text passwords.
        // YOU should use Bcrypt!
        // user.password = BcryptUtil.bcryptHash(password);
        user.password = password;
        user.role = role;
        user.persist();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
