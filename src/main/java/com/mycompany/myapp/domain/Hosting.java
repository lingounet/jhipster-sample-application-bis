package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Hosting.
 */
@Entity
@Table(name = "hosting")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Hosting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "date")
    private Instant date;

    @ManyToOne
    @JsonIgnoreProperties(value = { "hostings", "hostingType" }, allowSetters = true)
    private HostingPlatform hostingPlaform;

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

    public Hosting id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDate() {
        return this.date;
    }

    public Hosting date(Instant date) {
        this.setDate(date);
        return this;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public HostingPlatform getHostingPlaform() {
        return this.hostingPlaform;
    }

    public void setHostingPlaform(HostingPlatform hostingPlatform) {
        this.hostingPlaform = hostingPlatform;
    }

    public Hosting hostingPlaform(HostingPlatform hostingPlatform) {
        this.setHostingPlaform(hostingPlatform);
        return this;
    }

    public SecurityInterview getSecurityInterview() {
        return this.securityInterview;
    }

    public void setSecurityInterview(SecurityInterview securityInterview) {
        this.securityInterview = securityInterview;
    }

    public Hosting securityInterview(SecurityInterview securityInterview) {
        this.setSecurityInterview(securityInterview);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Hosting)) {
            return false;
        }
        return id != null && id.equals(((Hosting) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Hosting{" +
            "id=" + getId() +
            ", date='" + getDate() + "'" +
            "}";
    }
}
