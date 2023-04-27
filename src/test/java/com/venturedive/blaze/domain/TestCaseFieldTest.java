package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestCaseFieldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCaseField.class);
        TestCaseField testCaseField1 = new TestCaseField();
        testCaseField1.setId(1L);
        TestCaseField testCaseField2 = new TestCaseField();
        testCaseField2.setId(testCaseField1.getId());
        assertThat(testCaseField1).isEqualTo(testCaseField2);
        testCaseField2.setId(2L);
        assertThat(testCaseField1).isNotEqualTo(testCaseField2);
        testCaseField1.setId(null);
        assertThat(testCaseField1).isNotEqualTo(testCaseField2);
    }
}
