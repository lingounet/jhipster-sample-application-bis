package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.Process;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SecurityInterview.
 */
@Entity
@Table(name = "security_interview")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SecurityInterview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "application_name", nullable = false)
    private String applicationName;

    @NotNull
    @Column(name = "so", nullable = false)
    private String so;

    @Enumerated(EnumType.STRING)
    @Column(name = "process")
    private Process process;

    @JsonIgnoreProperties(value = { "securityInterview" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Psat psat;

    @JsonIgnoreProperties(value = { "securityInterview" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Availability availability;

    @OneToMany(mappedBy = "securityInterview")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "hostingPlaform", "securityInterview" }, allowSetters = true)
    private Set<Hosting> hostings = new HashSet<>();

    @OneToMany(mappedBy = "securityInterview")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "personalDataType", "securityInterview" }, allowSetters = true)
    private Set<PersonalData> personalData = new HashSet<>();

    @OneToMany(mappedBy = "securityInterview")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "icrfStatus", "securityInterview" }, allowSetters = true)
    private Set<Icrf> icrfs = new HashSet<>();

    @OneToMany(mappedBy = "securityInterview")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "sensitiveDataType", "securityInterview" }, allowSetters = true)
    private Set<SensitiveData> sensitiveData = new HashSet<>();

    @OneToMany(mappedBy = "securityInterview")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "securityInterview" }, allowSetters = true)
    private Set<ComplementaryQuestion> complementaryQuestions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "securityInterviews" }, allowSetters = true)
    private ApplicationType applicationType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SecurityInterview id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationName() {
        return this.applicationName;
    }

    public SecurityInterview applicationName(String applicationName) {
        this.setApplicationName(applicationName);
        return this;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getSo() {
        return this.so;
    }

    public SecurityInterview so(String so) {
        this.setSo(so);
        return this;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public Process getProcess() {
        return this.process;
    }

    public SecurityInterview process(Process process) {
        this.setProcess(process);
        return this;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public Psat getPsat() {
        return this.psat;
    }

    public void setPsat(Psat psat) {
        this.psat = psat;
    }

    public SecurityInterview psat(Psat psat) {
        this.setPsat(psat);
        return this;
    }

    public Availability getAvailability() {
        return this.availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public SecurityInterview availability(Availability availability) {
        this.setAvailability(availability);
        return this;
    }

    public Set<Hosting> getHostings() {
        return this.hostings;
    }

    public void setHostings(Set<Hosting> hostings) {
        if (this.hostings != null) {
            this.hostings.forEach(i -> i.setSecurityInterview(null));
        }
        if (hostings != null) {
            hostings.forEach(i -> i.setSecurityInterview(this));
        }
        this.hostings = hostings;
    }

    public SecurityInterview hostings(Set<Hosting> hostings) {
        this.setHostings(hostings);
        return this;
    }

    public SecurityInterview addHosting(Hosting hosting) {
        this.hostings.add(hosting);
        hosting.setSecurityInterview(this);
        return this;
    }

    public SecurityInterview removeHosting(Hosting hosting) {
        this.hostings.remove(hosting);
        hosting.setSecurityInterview(null);
        return this;
    }

    public Set<PersonalData> getPersonalData() {
        return this.personalData;
    }

    public void setPersonalData(Set<PersonalData> personalData) {
        if (this.personalData != null) {
            this.personalData.forEach(i -> i.setSecurityInterview(null));
        }
        if (personalData != null) {
            personalData.forEach(i -> i.setSecurityInterview(this));
        }
        this.personalData = personalData;
    }

    public SecurityInterview personalData(Set<PersonalData> personalData) {
        this.setPersonalData(personalData);
        return this;
    }

    public SecurityInterview addPersonalData(PersonalData personalData) {
        this.personalData.add(personalData);
        personalData.setSecurityInterview(this);
        return this;
    }

    public SecurityInterview removePersonalData(PersonalData personalData) {
        this.personalData.remove(personalData);
        personalData.setSecurityInterview(null);
        return this;
    }

    public Set<Icrf> getIcrfs() {
        return this.icrfs;
    }

    public void setIcrfs(Set<Icrf> icrfs) {
        if (this.icrfs != null) {
            this.icrfs.forEach(i -> i.setSecurityInterview(null));
        }
        if (icrfs != null) {
            icrfs.forEach(i -> i.setSecurityInterview(this));
        }
        this.icrfs = icrfs;
    }

    public SecurityInterview icrfs(Set<Icrf> icrfs) {
        this.setIcrfs(icrfs);
        return this;
    }

    public SecurityInterview addIcrf(Icrf icrf) {
        this.icrfs.add(icrf);
        icrf.setSecurityInterview(this);
        return this;
    }

    public SecurityInterview removeIcrf(Icrf icrf) {
        this.icrfs.remove(icrf);
        icrf.setSecurityInterview(null);
        return this;
    }

    public Set<SensitiveData> getSensitiveData() {
        return this.sensitiveData;
    }

    public void setSensitiveData(Set<SensitiveData> sensitiveData) {
        if (this.sensitiveData != null) {
            this.sensitiveData.forEach(i -> i.setSecurityInterview(null));
        }
        if (sensitiveData != null) {
            sensitiveData.forEach(i -> i.setSecurityInterview(this));
        }
        this.sensitiveData = sensitiveData;
    }

    public SecurityInterview sensitiveData(Set<SensitiveData> sensitiveData) {
        this.setSensitiveData(sensitiveData);
        return this;
    }

    public SecurityInterview addSensitiveData(SensitiveData sensitiveData) {
        this.sensitiveData.add(sensitiveData);
        sensitiveData.setSecurityInterview(this);
        return this;
    }

    public SecurityInterview removeSensitiveData(SensitiveData sensitiveData) {
        this.sensitiveData.remove(sensitiveData);
        sensitiveData.setSecurityInterview(null);
        return this;
    }

    public Set<ComplementaryQuestion> getComplementaryQuestions() {
        return this.complementaryQuestions;
    }

    public void setComplementaryQuestions(Set<ComplementaryQuestion> complementaryQuestions) {
        if (this.complementaryQuestions != null) {
            this.complementaryQuestions.forEach(i -> i.setSecurityInterview(null));
        }
        if (complementaryQuestions != null) {
            complementaryQuestions.forEach(i -> i.setSecurityInterview(this));
        }
        this.complementaryQuestions = complementaryQuestions;
    }

    public SecurityInterview complementaryQuestions(Set<ComplementaryQuestion> complementaryQuestions) {
        this.setComplementaryQuestions(complementaryQuestions);
        return this;
    }

    public SecurityInterview addComplementaryQuestion(ComplementaryQuestion complementaryQuestion) {
        this.complementaryQuestions.add(complementaryQuestion);
        complementaryQuestion.setSecurityInterview(this);
        return this;
    }

    public SecurityInterview removeComplementaryQuestion(ComplementaryQuestion complementaryQuestion) {
        this.complementaryQuestions.remove(complementaryQuestion);
        complementaryQuestion.setSecurityInterview(null);
        return this;
    }

    public ApplicationType getApplicationType() {
        return this.applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public SecurityInterview applicationType(ApplicationType applicationType) {
        this.setApplicationType(applicationType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SecurityInterview)) {
            return false;
        }
        return id != null && id.equals(((SecurityInterview) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SecurityInterview{" +
            "id=" + getId() +
            ", applicationName='" + getApplicationName() + "'" +
            ", so='" + getSo() + "'" +
            ", process='" + getProcess() + "'" +
            "}";
    }
}
