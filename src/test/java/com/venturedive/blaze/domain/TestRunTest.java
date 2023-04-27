package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestRunTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestRun.class);
        TestRun testRun1 = new TestRun();
        testRun1.setId(1L);
        TestRun testRun2 = new TestRun();
        testRun2.setId(testRun1.getId());
        assertThat(testRun1).isEqualTo(testRun2);
        testRun2.setId(2L);
        assertThat(testRun1).isNotEqualTo(testRun2);
        testRun1.setId(null);
        assertThat(testRun1).isNotEqualTo(testRun2);
    }
}
