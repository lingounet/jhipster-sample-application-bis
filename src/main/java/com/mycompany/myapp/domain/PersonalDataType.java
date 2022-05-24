package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PersonalDataType.
 */
@Entity
@Table(name = "personal_data_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PersonalDataType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "personalDataType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "personalDataType", "identity" }, allowSetters = true)
    private Set<PersonalData> personalData = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "personalDataTypes" }, allowSetters = true)
    private PersonalDataRegion personalDataRegion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PersonalDataType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PersonalDataType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PersonalData> getPersonalData() {
        return this.personalData;
    }

    public void setPersonalData(Set<PersonalData> personalData) {
        if (this.personalData != null) {
            this.personalData.forEach(i -> i.setPersonalDataType(null));
        }
        if (personalData != null) {
            personalData.forEach(i -> i.setPersonalDataType(this));
        }
        this.personalData = personalData;
    }

    public PersonalDataType personalData(Set<PersonalData> personalData) {
        this.setPersonalData(personalData);
        return this;
    }

    public PersonalDataType addPersonalData(PersonalData personalData) {
        this.personalData.add(personalData);
        personalData.setPersonalDataType(this);
        return this;
    }

    public PersonalDataType removePersonalData(PersonalData personalData) {
        this.personalData.remove(personalData);
        personalData.setPersonalDataType(null);
        return this;
    }

    public PersonalDataRegion getPersonalDataRegion() {
        return this.personalDataRegion;
    }

    public void setPersonalDataRegion(PersonalDataRegion personalDataRegion) {
        this.personalDataRegion = personalDataRegion;
    }

    public PersonalDataType personalDataRegion(PersonalDataRegion personalDataRegion) {
        this.setPersonalDataRegion(personalDataRegion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonalDataType)) {
            return false;
        }
        return id != null && id.equals(((PersonalDataType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalDataType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
