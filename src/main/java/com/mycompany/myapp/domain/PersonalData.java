package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PersonalData.
 */
@Entity
@Table(name = "personal_data")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PersonalData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Instant date;

    @ManyToOne
    @JsonIgnoreProperties(value = { "personalData", "personalDataRegion" }, allowSetters = true)
    private PersonalDataType personalDataType;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "availability", "applicationTypes", "hostings", "personalData", "icrfs", "sensitiveData", "complementaryQuestions", "psat",
        },
        allowSetters = true
    )
    private SecurityInterview securityInterview;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PersonalData id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public PersonalData date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public PersonalDataType getPersonalDataType() {
        return this.personalDataType;
    }

    public void setPersonalDataType(PersonalDataType personalDataType) {
        this.personalDataType = personalDataType;
    }

    public PersonalData personalDataType(PersonalDataType personalDataType) {
        this.setPersonalDataType(personalDataType);
        return this;
    }

    public SecurityInterview getSecurityInterview() {
        return this.securityInterview;
    }

    public void setSecurityInterview(SecurityInterview securityInterview) {
        this.securityInterview = securityInterview;
    }

    public PersonalData securityInterview(SecurityInterview securityInterview) {
        this.setSecurityInterview(securityInterview);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonalData)) {
            return false;
        }
        return id != null && id.equals(((PersonalData) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalData{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
