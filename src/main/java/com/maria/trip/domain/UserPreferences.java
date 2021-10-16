package com.maria.trip.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserPreferences.
 */
@Entity
@Table(name = "user_preferences")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserPreferences implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "language", length = 50, nullable = false)
    private String language;

    @NotNull
    @Size(max = 50)
    @Column(name = "currency", length = 50, nullable = false)
    private String currency;

    @Size(max = 50)
    @Column(name = "time_zone", length = 50)
    private String timeZone;

    @ManyToOne
    private User internalUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserPreferences id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguage() {
        return this.language;
    }

    public UserPreferences language(String language) {
        this.setLanguage(language);
        return this;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurrency() {
        return this.currency;
    }

    public UserPreferences currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTimeZone() {
        return this.timeZone;
    }

    public UserPreferences timeZone(String timeZone) {
        this.setTimeZone(timeZone);
        return this;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public User getInternalUser() {
        return this.internalUser;
    }

    public void setInternalUser(User user) {
        this.internalUser = user;
    }

    public UserPreferences internalUser(User user) {
        this.setInternalUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserPreferences)) {
            return false;
        }
        return id != null && id.equals(((UserPreferences) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserPreferences{" +
            "id=" + getId() +
            ", language='" + getLanguage() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", timeZone='" + getTimeZone() + "'" +
            "}";
    }
}
