package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Icrf.
 */
@Entity
@Table(name = "icrf")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Icrf implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Instant date;

    @ManyToOne
    @JsonIgnoreProperties(value = { "icrfs" }, allowSetters = true)
    private IcrfAssessment icrfAssessment;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "psat", "availability", "hostings", "personalData", "icrfs", "sensitiveData", "complementaryQuestions", "applicationType",
        },
        allowSetters = true
    )
    private Identity identity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Icrf id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Icrf date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public IcrfAssessment getIcrfAssessment() {
        return this.icrfAssessment;
    }

    public void setIcrfAssessment(IcrfAssessment icrfAssessment) {
        this.icrfAssessment = icrfAssessment;
    }

    public Icrf icrfAssessment(IcrfAssessment icrfAssessment) {
        this.setIcrfAssessment(icrfAssessment);
        return this;
    }

    public Identity getIdentity() {
        return this.identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public Icrf identity(Identity identity) {
        this.setIdentity(identity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Icrf)) {
            return false;
        }
        return id != null && id.equals(((Icrf) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Icrf{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
