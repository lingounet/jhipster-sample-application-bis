package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ApplicationType.
 */
@Entity
@Table(name = "application_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ApplicationType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "applicationType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = {
            "psat", "availability", "hostings", "personalData", "icrfs", "sensitiveData", "complementaryQuestions", "applicationType",
        },
        allowSetters = true
    )
    private Set<Identity> identities = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ApplicationType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ApplicationType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Identity> getIdentities() {
        return this.identities;
    }

    public void setIdentities(Set<Identity> identities) {
        if (this.identities != null) {
            this.identities.forEach(i -> i.setApplicationType(null));
        }
        if (identities != null) {
            identities.forEach(i -> i.setApplicationType(this));
        }
        this.identities = identities;
    }

    public ApplicationType identities(Set<Identity> identities) {
        this.setIdentities(identities);
        return this;
    }

    public ApplicationType addIdentity(Identity identity) {
        this.identities.add(identity);
        identity.setApplicationType(this);
        return this;
    }

    public ApplicationType removeIdentity(Identity identity) {
        this.identities.remove(identity);
        identity.setApplicationType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ApplicationType)) {
            return false;
        }
        return id != null && id.equals(((ApplicationType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ApplicationType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
