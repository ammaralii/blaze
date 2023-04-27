package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestCaseFieldAttachmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCaseFieldAttachment.class);
        TestCaseFieldAttachment testCaseFieldAttachment1 = new TestCaseFieldAttachment();
        testCaseFieldAttachment1.setId(1L);
        TestCaseFieldAttachment testCaseFieldAttachment2 = new TestCaseFieldAttachment();
        testCaseFieldAttachment2.setId(testCaseFieldAttachment1.getId());
        assertThat(testCaseFieldAttachment1).isEqualTo(testCaseFieldAttachment2);
        testCaseFieldAttachment2.setId(2L);
        assertThat(testCaseFieldAttachment1).isNotEqualTo(testCaseFieldAttachment2);
        testCaseFieldAttachment1.setId(null);
        assertThat(testCaseFieldAttachment1).isNotEqualTo(testCaseFieldAttachment2);
    }
}
