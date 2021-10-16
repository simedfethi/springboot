package com.maria.trip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TripCategory.
 */
@Entity
@Table(name = "trip_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TripCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "categoryname", length = 50)
    private String categoryname;

    @OneToMany(mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tripmaster", "category" }, allowSetters = true)
    private Set<Tripdetail> tripdetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TripCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryname() {
        return this.categoryname;
    }

    public TripCategory categoryname(String categoryname) {
        this.setCategoryname(categoryname);
        return this;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public Set<Tripdetail> getTripdetails() {
        return this.tripdetails;
    }

    public void setTripdetails(Set<Tripdetail> tripdetails) {
        if (this.tripdetails != null) {
            this.tripdetails.forEach(i -> i.setCategory(null));
        }
        if (tripdetails != null) {
            tripdetails.forEach(i -> i.setCategory(this));
        }
        this.tripdetails = tripdetails;
    }

    public TripCategory tripdetails(Set<Tripdetail> tripdetails) {
        this.setTripdetails(tripdetails);
        return this;
    }

    public TripCategory addTripdetail(Tripdetail tripdetail) {
        this.tripdetails.add(tripdetail);
        tripdetail.setCategory(this);
        return this;
    }

    public TripCategory removeTripdetail(Tripdetail tripdetail) {
        this.tripdetails.remove(tripdetail);
        tripdetail.setCategory(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TripCategory)) {
            return false;
        }
        return id != null && id.equals(((TripCategory) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TripCategory{" +
            "id=" + getId() +
            ", categoryname='" + getCategoryname() + "'" +
            "}";
    }
}
