package com.maria.trip.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TripGroups.
 */
@Entity
@Table(name = "trip_groups")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TripGroups implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "title", length = 50)
    private String title;

    @Size(max = 50)
    @Column(name = "description", length = 50)
    private String description;

    @ManyToMany
    @JoinTable(
        name = "rel_trip_groups__internal_user",
        joinColumns = @JoinColumn(name = "trip_groups_id"),
        inverseJoinColumns = @JoinColumn(name = "internal_user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> internalUsers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TripGroups id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public TripGroups title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public TripGroups description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<User> getInternalUsers() {
        return this.internalUsers;
    }

    public void setInternalUsers(Set<User> users) {
        this.internalUsers = users;
    }

    public TripGroups internalUsers(Set<User> users) {
        this.setInternalUsers(users);
        return this;
    }

    public TripGroups addInternalUser(User user) {
        this.internalUsers.add(user);
        return this;
    }

    public TripGroups removeInternalUser(User user) {
        this.internalUsers.remove(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripGroups)) {
            return false;
        }
        return id != null && id.equals(((TripGroups) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripGroups{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
