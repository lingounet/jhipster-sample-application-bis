package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A HostingType.
 */
@Entity
@Table(name = "hosting_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class HostingType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "hostingType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "hostings", "hostingType" }, allowSetters = true)
    private Set<HostingPlatform> hostingPlatforms = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HostingType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HostingType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<HostingPlatform> getHostingPlatforms() {
        return this.hostingPlatforms;
    }

    public void setHostingPlatforms(Set<HostingPlatform> hostingPlatforms) {
        if (this.hostingPlatforms != null) {
            this.hostingPlatforms.forEach(i -> i.setHostingType(null));
        }
        if (hostingPlatforms != null) {
            hostingPlatforms.forEach(i -> i.setHostingType(this));
        }
        this.hostingPlatforms = hostingPlatforms;
    }

    public HostingType hostingPlatforms(Set<HostingPlatform> hostingPlatforms) {
        this.setHostingPlatforms(hostingPlatforms);
        return this;
    }

    public HostingType addHostingPlatform(HostingPlatform hostingPlatform) {
        this.hostingPlatforms.add(hostingPlatform);
        hostingPlatform.setHostingType(this);
        return this;
    }

    public HostingType removeHostingPlatform(HostingPlatform hostingPlatform) {
        this.hostingPlatforms.remove(hostingPlatform);
        hostingPlatform.setHostingType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HostingType)) {
            return false;
        }
        return id != null && id.equals(((HostingType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HostingType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
