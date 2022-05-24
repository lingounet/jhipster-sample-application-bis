package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Status;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Psat.
 */
@Entity
@Table(name = "psat")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Psat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "aml_id", nullable = false)
    private String amlId;

    @NotNull
    @Column(name = "owner", nullable = false)
    private String owner;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @JsonIgnoreProperties(
        value = {
            "psat", "availability", "hostings", "personalData", "icrfs", "sensitiveData", "complementaryQuestions", "applicationType",
        },
        allowSetters = true
    )
    @OneToOne(mappedBy = "psat")
    private Identity identity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Psat id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAmlId() {
        return this.amlId;
    }

    public Psat amlId(String amlId) {
        this.setAmlId(amlId);
        return this;
    }

    public void setAmlId(String amlId) {
        this.amlId = amlId;
    }

    public String getOwner() {
        return this.owner;
    }

    public Psat owner(String owner) {
        this.setOwner(owner);
        return this;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Status getStatus() {
        return this.status;
    }

    public Psat status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Identity getIdentity() {
        return this.identity;
    }

    public void setIdentity(Identity identity) {
        if (this.identity != null) {
            this.identity.setPsat(null);
        }
        if (identity != null) {
            identity.setPsat(this);
        }
        this.identity = identity;
    }

    public Psat identity(Identity identity) {
        this.setIdentity(identity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Psat)) {
            return false;
        }
        return id != null && id.equals(((Psat) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Psat{" +
            "id=" + getId() +
            ", amlId='" + getAmlId() + "'" +
            ", owner='" + getOwner() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
