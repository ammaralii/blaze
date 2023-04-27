package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestSuiteTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestSuite.class);
        TestSuite testSuite1 = new TestSuite();
        testSuite1.setId(1L);
        TestSuite testSuite2 = new TestSuite();
        testSuite2.setId(testSuite1.getId());
        assertThat(testSuite1).isEqualTo(testSuite2);
        testSuite2.setId(2L);
        assertThat(testSuite1).isNotEqualTo(testSuite2);
        testSuite1.setId(null);
        assertThat(testSuite1).isNotEqualTo(testSuite2);
    }
}
