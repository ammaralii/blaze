package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestRunDetailAttachment.
 */
@Entity
@Table(name = "test_run_detail_attachment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRunDetailAttachment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 65535)
    @Column(name = "url", length = 65535, nullable = false)
    private String url;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = { "testRun", "testCase", "status", "testrundetailattachmentTestrundetails", "testrunstepdetailsTestrundetails" },
        allowSetters = true
    )
    private TestRunDetails testRunDetail;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestRunDetailAttachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public TestRunDetailAttachment url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TestRunDetails getTestRunDetail() {
        return this.testRunDetail;
    }

    public void setTestRunDetail(TestRunDetails testRunDetails) {
        this.testRunDetail = testRunDetails;
    }

    public TestRunDetailAttachment testRunDetail(TestRunDetails testRunDetails) {
        this.setTestRunDetail(testRunDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestRunDetailAttachment)) {
            return false;
        }
        return id != null && id.equals(((TestRunDetailAttachment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRunDetailAttachment{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
