package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestCaseAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCaseAttachment.class);
        TestCaseAttachment testCaseAttachment1 = new TestCaseAttachment();
        testCaseAttachment1.setId(1L);
        TestCaseAttachment testCaseAttachment2 = new TestCaseAttachment();
        testCaseAttachment2.setId(testCaseAttachment1.getId());
        assertThat(testCaseAttachment1).isEqualTo(testCaseAttachment2);
        testCaseAttachment2.setId(2L);
        assertThat(testCaseAttachment1).isNotEqualTo(testCaseAttachment2);
        testCaseAttachment1.setId(null);
        assertThat(testCaseAttachment1).isNotEqualTo(testCaseAttachment2);
    }
}
