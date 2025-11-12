package enat.bank.utils;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public abstract class Auditable implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    @Schema(hidden = true)
    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Schema(hidden = true)
    @Column(name = "deleted")
    private boolean deleted = false;

    @Schema(hidden = true)
    @Column(name = "deleted_by")
    private String deletedBy;

    @Schema(hidden = true)
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    @Schema(hidden = true)
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    @Schema(hidden = true)
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Schema(hidden = true)
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Version
    @Column(name = "version")
    private long version;
}

