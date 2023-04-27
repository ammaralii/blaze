package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestRunDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestRunDetails.class);
        TestRunDetails testRunDetails1 = new TestRunDetails();
        testRunDetails1.setId(1L);
        TestRunDetails testRunDetails2 = new TestRunDetails();
        testRunDetails2.setId(testRunDetails1.getId());
        assertThat(testRunDetails1).isEqualTo(testRunDetails2);
        testRunDetails2.setId(2L);
        assertThat(testRunDetails1).isNotEqualTo(testRunDetails2);
        testRunDetails1.setId(null);
        assertThat(testRunDetails1).isNotEqualTo(testRunDetails2);
    }
}
