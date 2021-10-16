package com.maria.trip.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tripdetail.
 */
@Entity
@Table(name = "tripdetail")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tripdetail implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "minimum_list")
    private Integer minimumList;

    @Column(name = "maximum_list")
    private Integer maximumList;

    @Column(name = "created_date")
    private LocalDate createdDate;

    @Column(name = "lastupdated")
    private LocalDate lastupdated;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "content_date")
    private ZonedDateTime contentDate;

    @ManyToOne
    private User tripmaster;

    @ManyToOne
    @JsonIgnoreProperties(value = { "tripdetails" }, allowSetters = true)
    private TripCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tripdetail id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMinimumList() {
        return this.minimumList;
    }

    public Tripdetail minimumList(Integer minimumList) {
        this.setMinimumList(minimumList);
        return this;
    }

    public void setMinimumList(Integer minimumList) {
        this.minimumList = minimumList;
    }

    public Integer getMaximumList() {
        return this.maximumList;
    }

    public Tripdetail maximumList(Integer maximumList) {
        this.setMaximumList(maximumList);
        return this;
    }

    public void setMaximumList(Integer maximumList) {
        this.maximumList = maximumList;
    }

    public LocalDate getCreatedDate() {
        return this.createdDate;
    }

    public Tripdetail createdDate(LocalDate createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDate getLastupdated() {
        return this.lastupdated;
    }

    public Tripdetail lastupdated(LocalDate lastupdated) {
        this.setLastupdated(lastupdated);
        return this;
    }

    public void setLastupdated(LocalDate lastupdated) {
        this.lastupdated = lastupdated;
    }

    public LocalDate getDepartureDate() {
        return this.departureDate;
    }

    public Tripdetail departureDate(LocalDate departureDate) {
        this.setDepartureDate(departureDate);
        return this;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getArrivalDate() {
        return this.arrivalDate;
    }

    public Tripdetail arrivalDate(LocalDate arrivalDate) {
        this.setArrivalDate(arrivalDate);
        return this;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public ZonedDateTime getContentDate() {
        return this.contentDate;
    }

    public Tripdetail contentDate(ZonedDateTime contentDate) {
        this.setContentDate(contentDate);
        return this;
    }

    public void setContentDate(ZonedDateTime contentDate) {
        this.contentDate = contentDate;
    }

    public User getTripmaster() {
        return this.tripmaster;
    }

    public void setTripmaster(User user) {
        this.tripmaster = user;
    }

    public Tripdetail tripmaster(User user) {
        this.setTripmaster(user);
        return this;
    }

    public TripCategory getCategory() {
        return this.category;
    }

    public void setCategory(TripCategory tripCategory) {
        this.category = tripCategory;
    }

    public Tripdetail category(TripCategory tripCategory) {
        this.setCategory(tripCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tripdetail)) {
            return false;
        }
        return id != null && id.equals(((Tripdetail) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tripdetail{" +
            "id=" + getId() +
            ", minimumList=" + getMinimumList() +
            ", maximumList=" + getMaximumList() +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastupdated='" + getLastupdated() + "'" +
            ", departureDate='" + getDepartureDate() + "'" +
            ", arrivalDate='" + getArrivalDate() + "'" +
            ", contentDate='" + getContentDate() + "'" +
            "}";
    }
}
