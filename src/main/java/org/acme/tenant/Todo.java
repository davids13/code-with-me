package org.acme.tenant;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import org.hibernate.annotations.TenantId;

@Entity
@Table(name = "todos")
public class Todo extends PanacheEntity {
    public String title;
    public boolean completed = false;
    @TenantId // on the entity tells Hibernate what column to use.
    public String tenantId;

    public Todo() {
    }

    public Todo(String tenantId, String title) {
        this.tenantId = tenantId;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public String toString() {
        return "Todo{title='%s', completed=%s, tenantId='%s'}".formatted(title, completed, tenantId);
    }
}
