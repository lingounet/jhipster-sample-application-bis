package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IcrfStatus.
 */
@Entity
@Table(name = "icrf_status")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class IcrfStatus implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @JsonIgnoreProperties(value = { "icrfStatus", "securityInterview" }, allowSetters = true)
    @OneToOne(mappedBy = "icrfStatus")
    private Icrf icrf;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IcrfStatus id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public IcrfStatus name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Icrf getIcrf() {
        return this.icrf;
    }

    public void setIcrf(Icrf icrf) {
        if (this.icrf != null) {
            this.icrf.setIcrfStatus(null);
        }
        if (icrf != null) {
            icrf.setIcrfStatus(this);
        }
        this.icrf = icrf;
    }

    public IcrfStatus icrf(Icrf icrf) {
        this.setIcrf(icrf);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IcrfStatus)) {
            return false;
        }
        return id != null && id.equals(((IcrfStatus) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IcrfStatus{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
