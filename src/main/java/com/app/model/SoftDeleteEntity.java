package com.app.model;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@FilterDef(name = "softDeleteFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
public abstract class SoftDeleteEntity extends BaseEntity {

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * Marks the entity as soft-deleted by setting the deletedAt timestamp.
     */
    public void softDelete() { this.deletedAt = LocalDateTime.now(); }
    /**
     * Restores a soft-deleted entity by setting deletedAt to null.
     */
    public void restore() { this.deletedAt = null; }
    /**
     * Checks if the entity is currently soft-deleted.
     * @return true if deletedAt is not null, false otherwise.
     */
    public boolean isDeleted() { return deletedAt != null; }
}