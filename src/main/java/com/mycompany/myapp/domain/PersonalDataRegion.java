package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PersonalDataRegion.
 */
@Entity
@Table(name = "personal_data_region")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PersonalDataRegion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "personalDataRegion")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "personalData", "personalDataRegion" }, allowSetters = true)
    private Set<PersonalDataType> personalDataTypes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PersonalDataRegion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PersonalDataRegion name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PersonalDataType> getPersonalDataTypes() {
        return this.personalDataTypes;
    }

    public void setPersonalDataTypes(Set<PersonalDataType> personalDataTypes) {
        if (this.personalDataTypes != null) {
            this.personalDataTypes.forEach(i -> i.setPersonalDataRegion(null));
        }
        if (personalDataTypes != null) {
            personalDataTypes.forEach(i -> i.setPersonalDataRegion(this));
        }
        this.personalDataTypes = personalDataTypes;
    }

    public PersonalDataRegion personalDataTypes(Set<PersonalDataType> personalDataTypes) {
        this.setPersonalDataTypes(personalDataTypes);
        return this;
    }

    public PersonalDataRegion addPersonalDataType(PersonalDataType personalDataType) {
        this.personalDataTypes.add(personalDataType);
        personalDataType.setPersonalDataRegion(this);
        return this;
    }

    public PersonalDataRegion removePersonalDataType(PersonalDataType personalDataType) {
        this.personalDataTypes.remove(personalDataType);
        personalDataType.setPersonalDataRegion(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PersonalDataRegion)) {
            return false;
        }
        return id != null && id.equals(((PersonalDataRegion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PersonalDataRegion{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
