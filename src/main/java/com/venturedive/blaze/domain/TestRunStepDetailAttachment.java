package com.venturedive.blaze.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A TestRunStepDetailAttachment.
 */
@Entity
@Table(name = "test_run_step_detail_attachment")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TestRunStepDetailAttachment implements Serializable {

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
        value = { "testRunDetail", "stepDetail", "status", "testrunstepdetailattachmentTestrunstepdetails" },
        allowSetters = true
    )
    private TestRunStepDetails testRunStepDetail;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TestRunStepDetailAttachment id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public TestRunStepDetailAttachment url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public TestRunStepDetails getTestRunStepDetail() {
        return this.testRunStepDetail;
    }

    public void setTestRunStepDetail(TestRunStepDetails testRunStepDetails) {
        this.testRunStepDetail = testRunStepDetails;
    }

    public TestRunStepDetailAttachment testRunStepDetail(TestRunStepDetails testRunStepDetails) {
        this.setTestRunStepDetail(testRunStepDetails);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TestRunStepDetailAttachment)) {
            return false;
        }
        return id != null && id.equals(((TestRunStepDetailAttachment) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TestRunStepDetailAttachment{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            "}";
    }
}
