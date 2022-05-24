package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SensitiveDataType.
 */
@Entity
@Table(name = "sensitive_data_type")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SensitiveDataType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "sensitiveDataType")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sensitiveDataType", "identity" }, allowSetters = true)
    private Set<SensitiveData> sensitiveData = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SensitiveDataType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public SensitiveDataType name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<SensitiveData> getSensitiveData() {
        return this.sensitiveData;
    }

    public void setSensitiveData(Set<SensitiveData> sensitiveData) {
        if (this.sensitiveData != null) {
            this.sensitiveData.forEach(i -> i.setSensitiveDataType(null));
        }
        if (sensitiveData != null) {
            sensitiveData.forEach(i -> i.setSensitiveDataType(this));
        }
        this.sensitiveData = sensitiveData;
    }

    public SensitiveDataType sensitiveData(Set<SensitiveData> sensitiveData) {
        this.setSensitiveData(sensitiveData);
        return this;
    }

    public SensitiveDataType addSensitiveData(SensitiveData sensitiveData) {
        this.sensitiveData.add(sensitiveData);
        sensitiveData.setSensitiveDataType(this);
        return this;
    }

    public SensitiveDataType removeSensitiveData(SensitiveData sensitiveData) {
        this.sensitiveData.remove(sensitiveData);
        sensitiveData.setSensitiveDataType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SensitiveDataType)) {
            return false;
        }
        return id != null && id.equals(((SensitiveDataType) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SensitiveDataType{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
