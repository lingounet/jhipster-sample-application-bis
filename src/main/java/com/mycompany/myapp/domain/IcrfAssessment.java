package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IcrfAssessment.
 */
@Entity
@Table(name = "icrf_assessment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IcrfAssessment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private Boolean status;

    @OneToMany(mappedBy = "icrfAssessment")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "icrfAssessment", "identity" }, allowSetters = true)
    private Set<Icrf> icrfs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IcrfAssessment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public IcrfAssessment code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public IcrfAssessment description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStatus() {
        return this.status;
    }

    public IcrfAssessment status(Boolean status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Set<Icrf> getIcrfs() {
        return this.icrfs;
    }

    public void setIcrfs(Set<Icrf> icrfs) {
        if (this.icrfs != null) {
            this.icrfs.forEach(i -> i.setIcrfAssessment(null));
        }
        if (icrfs != null) {
            icrfs.forEach(i -> i.setIcrfAssessment(this));
        }
        this.icrfs = icrfs;
    }

    public IcrfAssessment icrfs(Set<Icrf> icrfs) {
        this.setIcrfs(icrfs);
        return this;
    }

    public IcrfAssessment addIcrf(Icrf icrf) {
        this.icrfs.add(icrf);
        icrf.setIcrfAssessment(this);
        return this;
    }

    public IcrfAssessment removeIcrf(Icrf icrf) {
        this.icrfs.remove(icrf);
        icrf.setIcrfAssessment(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IcrfAssessment)) {
            return false;
        }
        return id != null && id.equals(((IcrfAssessment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IcrfAssessment{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
