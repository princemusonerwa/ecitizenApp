package com.minaloc.gov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Attachment.
 */
@Entity
@Table(name = "attachment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Attachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "attachment_link", nullable = false)
    private byte[] attachmentLink;

    @NotNull
    @Column(name = "attachment_link_content_type", nullable = false)
    private String attachmentLinkContentType;

    @NotNull
    @Column(name = "attachment_type", nullable = false)
    private String attachmentType;

    @ManyToOne
    @JsonIgnoreProperties(value = { "category", "umuturage", "user", "organizations" }, allowSetters = true)
    private Complain complain;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Attachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getAttachmentLink() {
        return this.attachmentLink;
    }

    public Attachment attachmentLink(byte[] attachmentLink) {
        this.setAttachmentLink(attachmentLink);
        return this;
    }

    public void setAttachmentLink(byte[] attachmentLink) {
        this.attachmentLink = attachmentLink;
    }

    public String getAttachmentLinkContentType() {
        return this.attachmentLinkContentType;
    }

    public Attachment attachmentLinkContentType(String attachmentLinkContentType) {
        this.attachmentLinkContentType = attachmentLinkContentType;
        return this;
    }

    public void setAttachmentLinkContentType(String attachmentLinkContentType) {
        this.attachmentLinkContentType = attachmentLinkContentType;
    }

    public String getAttachmentType() {
        return this.attachmentType;
    }

    public Attachment attachmentType(String attachmentType) {
        this.setAttachmentType(attachmentType);
        return this;
    }

    public void setAttachmentType(String attachmentType) {
        this.attachmentType = attachmentType;
    }

    public Complain getComplain() {
        return this.complain;
    }

    public void setComplain(Complain complain) {
        this.complain = complain;
    }

    public Attachment complain(Complain complain) {
        this.setComplain(complain);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attachment)) {
            return false;
        }
        return id != null && id.equals(((Attachment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attachment{" +
            "id=" + getId() +
            ", attachmentLink='" + getAttachmentLink() + "'" +
            ", attachmentLinkContentType='" + getAttachmentLinkContentType() + "'" +
            ", attachmentType='" + getAttachmentType() + "'" +
            "}";
    }
}
