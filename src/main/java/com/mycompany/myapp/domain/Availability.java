package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Criticality;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Availability.
 */
@Entity
@Table(name = "availability")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Availability implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "financial")
    private Criticality financial;

    @Enumerated(EnumType.STRING)
    @Column(name = "legal")
    private Criticality legal;

    @Enumerated(EnumType.STRING)
    @Column(name = "image")
    private Criticality image;

    @Enumerated(EnumType.STRING)
    @Column(name = "strategy")
    private Criticality strategy;

    @Enumerated(EnumType.STRING)
    @Column(name = "operational")
    private Criticality operational;

    @Lob
    @Column(name = "traceability")
    private byte[] traceability;

    @Column(name = "traceability_content_type")
    private String traceabilityContentType;

    @Lob
    @Column(name = "confidentiality")
    private byte[] confidentiality;

    @Column(name = "confidentiality_content_type")
    private String confidentialityContentType;

    @Lob
    @Column(name = "integrity")
    private byte[] integrity;

    @Column(name = "integrity_content_type")
    private String integrityContentType;

    @Column(name = "critical")
    private Boolean critical;

    @JsonIgnoreProperties(
        value = {
            "psat", "availability", "hostings", "personalData", "icrfs", "sensitiveData", "complementaryQuestions", "applicationType",
        },
        allowSetters = true
    )
    @OneToOne(mappedBy = "availability")
    private Identity identity;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Availability id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Criticality getFinancial() {
        return this.financial;
    }

    public Availability financial(Criticality financial) {
        this.setFinancial(financial);
        return this;
    }

    public void setFinancial(Criticality financial) {
        this.financial = financial;
    }

    public Criticality getLegal() {
        return this.legal;
    }

    public Availability legal(Criticality legal) {
        this.setLegal(legal);
        return this;
    }

    public void setLegal(Criticality legal) {
        this.legal = legal;
    }

    public Criticality getImage() {
        return this.image;
    }

    public Availability image(Criticality image) {
        this.setImage(image);
        return this;
    }

    public void setImage(Criticality image) {
        this.image = image;
    }

    public Criticality getStrategy() {
        return this.strategy;
    }

    public Availability strategy(Criticality strategy) {
        this.setStrategy(strategy);
        return this;
    }

    public void setStrategy(Criticality strategy) {
        this.strategy = strategy;
    }

    public Criticality getOperational() {
        return this.operational;
    }

    public Availability operational(Criticality operational) {
        this.setOperational(operational);
        return this;
    }

    public void setOperational(Criticality operational) {
        this.operational = operational;
    }

    public byte[] getTraceability() {
        return this.traceability;
    }

    public Availability traceability(byte[] traceability) {
        this.setTraceability(traceability);
        return this;
    }

    public void setTraceability(byte[] traceability) {
        this.traceability = traceability;
    }

    public String getTraceabilityContentType() {
        return this.traceabilityContentType;
    }

    public Availability traceabilityContentType(String traceabilityContentType) {
        this.traceabilityContentType = traceabilityContentType;
        return this;
    }

    public void setTraceabilityContentType(String traceabilityContentType) {
        this.traceabilityContentType = traceabilityContentType;
    }

    public byte[] getConfidentiality() {
        return this.confidentiality;
    }

    public Availability confidentiality(byte[] confidentiality) {
        this.setConfidentiality(confidentiality);
        return this;
    }

    public void setConfidentiality(byte[] confidentiality) {
        this.confidentiality = confidentiality;
    }

    public String getConfidentialityContentType() {
        return this.confidentialityContentType;
    }

    public Availability confidentialityContentType(String confidentialityContentType) {
        this.confidentialityContentType = confidentialityContentType;
        return this;
    }

    public void setConfidentialityContentType(String confidentialityContentType) {
        this.confidentialityContentType = confidentialityContentType;
    }

    public byte[] getIntegrity() {
        return this.integrity;
    }

    public Availability integrity(byte[] integrity) {
        this.setIntegrity(integrity);
        return this;
    }

    public void setIntegrity(byte[] integrity) {
        this.integrity = integrity;
    }

    public String getIntegrityContentType() {
        return this.integrityContentType;
    }

    public Availability integrityContentType(String integrityContentType) {
        this.integrityContentType = integrityContentType;
        return this;
    }

    public void setIntegrityContentType(String integrityContentType) {
        this.integrityContentType = integrityContentType;
    }

    public Boolean getCritical() {
        return this.critical;
    }

    public Availability critical(Boolean critical) {
        this.setCritical(critical);
        return this;
    }

    public void setCritical(Boolean critical) {
        this.critical = critical;
    }

    public Identity getIdentity() {
        return this.identity;
    }

    public void setIdentity(Identity identity) {
        if (this.identity != null) {
            this.identity.setAvailability(null);
        }
        if (identity != null) {
            identity.setAvailability(this);
        }
        this.identity = identity;
    }

    public Availability identity(Identity identity) {
        this.setIdentity(identity);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Availability)) {
            return false;
        }
        return id != null && id.equals(((Availability) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Availability{" +
            "id=" + getId() +
            ", financial='" + getFinancial() + "'" +
            ", legal='" + getLegal() + "'" +
            ", image='" + getImage() + "'" +
            ", strategy='" + getStrategy() + "'" +
            ", operational='" + getOperational() + "'" +
            ", traceability='" + getTraceability() + "'" +
            ", traceabilityContentType='" + getTraceabilityContentType() + "'" +
            ", confidentiality='" + getConfidentiality() + "'" +
            ", confidentialityContentType='" + getConfidentialityContentType() + "'" +
            ", integrity='" + getIntegrity() + "'" +
            ", integrityContentType='" + getIntegrityContentType() + "'" +
            ", critical='" + getCritical() + "'" +
            "}";
    }
}
