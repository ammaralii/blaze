package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestStatus.class);
        TestStatus testStatus1 = new TestStatus();
        testStatus1.setId(1L);
        TestStatus testStatus2 = new TestStatus();
        testStatus2.setId(testStatus1.getId());
        assertThat(testStatus1).isEqualTo(testStatus2);
        testStatus2.setId(2L);
        assertThat(testStatus1).isNotEqualTo(testStatus2);
        testStatus1.setId(null);
        assertThat(testStatus1).isNotEqualTo(testStatus2);
    }
}
