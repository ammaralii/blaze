package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestRunStepDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestRunStepDetails.class);
        TestRunStepDetails testRunStepDetails1 = new TestRunStepDetails();
        testRunStepDetails1.setId(1L);
        TestRunStepDetails testRunStepDetails2 = new TestRunStepDetails();
        testRunStepDetails2.setId(testRunStepDetails1.getId());
        assertThat(testRunStepDetails1).isEqualTo(testRunStepDetails2);
        testRunStepDetails2.setId(2L);
        assertThat(testRunStepDetails1).isNotEqualTo(testRunStepDetails2);
        testRunStepDetails1.setId(null);
        assertThat(testRunStepDetails1).isNotEqualTo(testRunStepDetails2);
    }
}
