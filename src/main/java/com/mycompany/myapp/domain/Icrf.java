package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
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

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @JsonIgnoreProperties(value = { "icrf" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private IcrfStatus icrfStatus;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "psat", "availability", "hostings", "personalData", "icrfs", "sensitiveData", "complementaryQuestions", "applicationType",
        },
        allowSetters = true
    )
    private SecurityInterview securityInterview;

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

    public String getCode() {
        return this.code;
    }

    public Icrf code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return this.description;
    }

    public Icrf description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public IcrfStatus getIcrfStatus() {
        return this.icrfStatus;
    }

    public void setIcrfStatus(IcrfStatus icrfStatus) {
        this.icrfStatus = icrfStatus;
    }

    public Icrf icrfStatus(IcrfStatus icrfStatus) {
        this.setIcrfStatus(icrfStatus);
        return this;
    }

    public SecurityInterview getSecurityInterview() {
        return this.securityInterview;
    }

    public void setSecurityInterview(SecurityInterview securityInterview) {
        this.securityInterview = securityInterview;
    }

    public Icrf securityInterview(SecurityInterview securityInterview) {
        this.setSecurityInterview(securityInterview);
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
            ", code='" + getCode() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
