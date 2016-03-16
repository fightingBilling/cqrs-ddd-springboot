package org.dsinczak.boot.common.ddd.support.domain;

import org.springframework.context.annotation.Scope;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
@Scope("prototype")
//created in domain factories, not in spring container, therefore we don't want eager creation
@MappedSuperclass
@EntityListeners(value = AuditingEntityListener.class)
public class BaseAggregateRoot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false)
    private long id;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    @Version
    private Long version;

    @Enumerated(EnumType.STRING)
    private AggregateStatus aggregateStatus = AggregateStatus.ACTIVE;

    public long getId() {
        return id;
    }

    public void markAsRemoved() {
        aggregateStatus = AggregateStatus.ARCHIVE;
    }

    public boolean isRemoved() {
        return aggregateStatus == AggregateStatus.ARCHIVE;
    }

    protected LocalDateTime getCreatedDate() {
        return createdDate;
    }

    protected Long getVersion() {
        return version;
    }

    protected LocalDateTime getLastModifiedDate() {
        return modifiedDate;
    }

    protected AggregateStatus getAggregateStatus() {
        return aggregateStatus;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final BaseAggregateRoot that = (BaseAggregateRoot) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public enum AggregateStatus {
        ACTIVE,
        ARCHIVE
    }
}
