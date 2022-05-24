package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ComplementaryQuestion.
 */
@Entity
@Table(name = "complementary_question")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ComplementaryQuestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "internet")
    private Boolean internet;

    @Column(name = "development")
    private Boolean development;

    @Column(name = "configuration")
    private Boolean configuration;

    @Column(name = "cloud")
    private Boolean cloud;

    @Column(name = "internal")
    private Boolean internal;

    @Column(name = "partner")
    private Boolean partner;

    @Column(name = "users")
    private Integer users;

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

    public ComplementaryQuestion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInternet() {
        return this.internet;
    }

    public ComplementaryQuestion internet(Boolean internet) {
        this.setInternet(internet);
        return this;
    }

    public void setInternet(Boolean internet) {
        this.internet = internet;
    }

    public Boolean getDevelopment() {
        return this.development;
    }

    public ComplementaryQuestion development(Boolean development) {
        this.setDevelopment(development);
        return this;
    }

    public void setDevelopment(Boolean development) {
        this.development = development;
    }

    public Boolean getConfiguration() {
        return this.configuration;
    }

    public ComplementaryQuestion configuration(Boolean configuration) {
        this.setConfiguration(configuration);
        return this;
    }

    public void setConfiguration(Boolean configuration) {
        this.configuration = configuration;
    }

    public Boolean getCloud() {
        return this.cloud;
    }

    public ComplementaryQuestion cloud(Boolean cloud) {
        this.setCloud(cloud);
        return this;
    }

    public void setCloud(Boolean cloud) {
        this.cloud = cloud;
    }

    public Boolean getInternal() {
        return this.internal;
    }

    public ComplementaryQuestion internal(Boolean internal) {
        this.setInternal(internal);
        return this;
    }

    public void setInternal(Boolean internal) {
        this.internal = internal;
    }

    public Boolean getPartner() {
        return this.partner;
    }

    public ComplementaryQuestion partner(Boolean partner) {
        this.setPartner(partner);
        return this;
    }

    public void setPartner(Boolean partner) {
        this.partner = partner;
    }

    public Integer getUsers() {
        return this.users;
    }

    public ComplementaryQuestion users(Integer users) {
        this.setUsers(users);
        return this;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public SecurityInterview getSecurityInterview() {
        return this.securityInterview;
    }

    public void setSecurityInterview(SecurityInterview securityInterview) {
        this.securityInterview = securityInterview;
    }

    public ComplementaryQuestion securityInterview(SecurityInterview securityInterview) {
        this.setSecurityInterview(securityInterview);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComplementaryQuestion)) {
            return false;
        }
        return id != null && id.equals(((ComplementaryQuestion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComplementaryQuestion{" +
            "id=" + getId() +
            ", internet='" + getInternet() + "'" +
            ", development='" + getDevelopment() + "'" +
            ", configuration='" + getConfiguration() + "'" +
            ", cloud='" + getCloud() + "'" +
            ", internal='" + getInternal() + "'" +
            ", partner='" + getPartner() + "'" +
            ", users=" + getUsers() +
            "}";
    }
}
