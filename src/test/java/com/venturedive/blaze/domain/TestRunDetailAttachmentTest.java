package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestRunDetailAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestRunDetailAttachment.class);
        TestRunDetailAttachment testRunDetailAttachment1 = new TestRunDetailAttachment();
        testRunDetailAttachment1.setId(1L);
        TestRunDetailAttachment testRunDetailAttachment2 = new TestRunDetailAttachment();
        testRunDetailAttachment2.setId(testRunDetailAttachment1.getId());
        assertThat(testRunDetailAttachment1).isEqualTo(testRunDetailAttachment2);
        testRunDetailAttachment2.setId(2L);
        assertThat(testRunDetailAttachment1).isNotEqualTo(testRunDetailAttachment2);
        testRunDetailAttachment1.setId(null);
        assertThat(testRunDetailAttachment1).isNotEqualTo(testRunDetailAttachment2);
    }
}
