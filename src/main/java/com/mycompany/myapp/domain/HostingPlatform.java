package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HostingPlatform.
 */
@Entity
@Table(name = "hosting_platform")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HostingPlatform implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "hostingPlaform")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "hostingPlaform", "securityInterview" }, allowSetters = true)
    private Set<Hosting> hostings = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "hostingPlatforms" }, allowSetters = true)
    private HostingType hostingType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HostingPlatform id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HostingPlatform name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Hosting> getHostings() {
        return this.hostings;
    }

    public void setHostings(Set<Hosting> hostings) {
        if (this.hostings != null) {
            this.hostings.forEach(i -> i.setHostingPlaform(null));
        }
        if (hostings != null) {
            hostings.forEach(i -> i.setHostingPlaform(this));
        }
        this.hostings = hostings;
    }

    public HostingPlatform hostings(Set<Hosting> hostings) {
        this.setHostings(hostings);
        return this;
    }

    public HostingPlatform addHosting(Hosting hosting) {
        this.hostings.add(hosting);
        hosting.setHostingPlaform(this);
        return this;
    }

    public HostingPlatform removeHosting(Hosting hosting) {
        this.hostings.remove(hosting);
        hosting.setHostingPlaform(null);
        return this;
    }

    public HostingType getHostingType() {
        return this.hostingType;
    }

    public void setHostingType(HostingType hostingType) {
        this.hostingType = hostingType;
    }

    public HostingPlatform hostingType(HostingType hostingType) {
        this.setHostingType(hostingType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HostingPlatform)) {
            return false;
        }
        return id != null && id.equals(((HostingPlatform) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HostingPlatform{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
