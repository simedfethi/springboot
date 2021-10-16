package com.maria.trip.domain;

import com.maria.trip.domain.enumeration.GovernmentIDType;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserGovernmentID.
 */
@Entity
@Table(name = "user_government_id")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserGovernmentID implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 50)
    @Column(name = "country", length = 50)
    private String country;

    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type")
    private GovernmentIDType docType;

    @Lob
    @Column(name = "doc_pic_rect")
    private byte[] docPicRect;

    @Column(name = "doc_pic_rect_content_type")
    private String docPicRectContentType;

    @Lob
    @Column(name = "doc_pic_vers")
    private byte[] docPicVers;

    @Column(name = "doc_pic_vers_content_type")
    private String docPicVersContentType;

    @Lob
    @Column(name = "doc_pic_inst")
    private byte[] docPicInst;

    @Column(name = "doc_pic_inst_content_type")
    private String docPicInstContentType;

    @Column(name = "id_verified")
    private Boolean idVerified;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserGovernmentID id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return this.country;
    }

    public UserGovernmentID country(String country) {
        this.setCountry(country);
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public GovernmentIDType getDocType() {
        return this.docType;
    }

    public UserGovernmentID docType(GovernmentIDType docType) {
        this.setDocType(docType);
        return this;
    }

    public void setDocType(GovernmentIDType docType) {
        this.docType = docType;
    }

    public byte[] getDocPicRect() {
        return this.docPicRect;
    }

    public UserGovernmentID docPicRect(byte[] docPicRect) {
        this.setDocPicRect(docPicRect);
        return this;
    }

    public void setDocPicRect(byte[] docPicRect) {
        this.docPicRect = docPicRect;
    }

    public String getDocPicRectContentType() {
        return this.docPicRectContentType;
    }

    public UserGovernmentID docPicRectContentType(String docPicRectContentType) {
        this.docPicRectContentType = docPicRectContentType;
        return this;
    }

    public void setDocPicRectContentType(String docPicRectContentType) {
        this.docPicRectContentType = docPicRectContentType;
    }

    public byte[] getDocPicVers() {
        return this.docPicVers;
    }

    public UserGovernmentID docPicVers(byte[] docPicVers) {
        this.setDocPicVers(docPicVers);
        return this;
    }

    public void setDocPicVers(byte[] docPicVers) {
        this.docPicVers = docPicVers;
    }

    public String getDocPicVersContentType() {
        return this.docPicVersContentType;
    }

    public UserGovernmentID docPicVersContentType(String docPicVersContentType) {
        this.docPicVersContentType = docPicVersContentType;
        return this;
    }

    public void setDocPicVersContentType(String docPicVersContentType) {
        this.docPicVersContentType = docPicVersContentType;
    }

    public byte[] getDocPicInst() {
        return this.docPicInst;
    }

    public UserGovernmentID docPicInst(byte[] docPicInst) {
        this.setDocPicInst(docPicInst);
        return this;
    }

    public void setDocPicInst(byte[] docPicInst) {
        this.docPicInst = docPicInst;
    }

    public String getDocPicInstContentType() {
        return this.docPicInstContentType;
    }

    public UserGovernmentID docPicInstContentType(String docPicInstContentType) {
        this.docPicInstContentType = docPicInstContentType;
        return this;
    }

    public void setDocPicInstContentType(String docPicInstContentType) {
        this.docPicInstContentType = docPicInstContentType;
    }

    public Boolean getIdVerified() {
        return this.idVerified;
    }

    public UserGovernmentID idVerified(Boolean idVerified) {
        this.setIdVerified(idVerified);
        return this;
    }

    public void setIdVerified(Boolean idVerified) {
        this.idVerified = idVerified;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserGovernmentID user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGovernmentID)) {
            return false;
        }
        return id != null && id.equals(((UserGovernmentID) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserGovernmentID{" +
            "id=" + getId() +
            ", country='" + getCountry() + "'" +
            ", docType='" + getDocType() + "'" +
            ", docPicRect='" + getDocPicRect() + "'" +
            ", docPicRectContentType='" + getDocPicRectContentType() + "'" +
            ", docPicVers='" + getDocPicVers() + "'" +
            ", docPicVersContentType='" + getDocPicVersContentType() + "'" +
            ", docPicInst='" + getDocPicInst() + "'" +
            ", docPicInstContentType='" + getDocPicInstContentType() + "'" +
            ", idVerified='" + getIdVerified() + "'" +
            "}";
    }
}
