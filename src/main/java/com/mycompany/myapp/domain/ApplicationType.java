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
    private Set<SecurityInterview> securityInterviews = new HashSet<>();

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

    public Set<SecurityInterview> getSecurityInterviews() {
        return this.securityInterviews;
    }

    public void setSecurityInterviews(Set<SecurityInterview> securityInterviews) {
        if (this.securityInterviews != null) {
            this.securityInterviews.forEach(i -> i.setApplicationType(null));
        }
        if (securityInterviews != null) {
            securityInterviews.forEach(i -> i.setApplicationType(this));
        }
        this.securityInterviews = securityInterviews;
    }

    public ApplicationType securityInterviews(Set<SecurityInterview> securityInterviews) {
        this.setSecurityInterviews(securityInterviews);
        return this;
    }

    public ApplicationType addSecurityInterview(SecurityInterview securityInterview) {
        this.securityInterviews.add(securityInterview);
        securityInterview.setApplicationType(this);
        return this;
    }

    public ApplicationType removeSecurityInterview(SecurityInterview securityInterview) {
        this.securityInterviews.remove(securityInterview);
        securityInterview.setApplicationType(null);
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
