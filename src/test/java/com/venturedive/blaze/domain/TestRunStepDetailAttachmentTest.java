package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestRunStepDetailAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestRunStepDetailAttachment.class);
        TestRunStepDetailAttachment testRunStepDetailAttachment1 = new TestRunStepDetailAttachment();
        testRunStepDetailAttachment1.setId(1L);
        TestRunStepDetailAttachment testRunStepDetailAttachment2 = new TestRunStepDetailAttachment();
        testRunStepDetailAttachment2.setId(testRunStepDetailAttachment1.getId());
        assertThat(testRunStepDetailAttachment1).isEqualTo(testRunStepDetailAttachment2);
        testRunStepDetailAttachment2.setId(2L);
        assertThat(testRunStepDetailAttachment1).isNotEqualTo(testRunStepDetailAttachment2);
        testRunStepDetailAttachment1.setId(null);
        assertThat(testRunStepDetailAttachment1).isNotEqualTo(testRunStepDetailAttachment2);
    }
}
