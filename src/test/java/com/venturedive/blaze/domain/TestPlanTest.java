package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestPlanTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestPlan.class);
        TestPlan testPlan1 = new TestPlan();
        testPlan1.setId(1L);
        TestPlan testPlan2 = new TestPlan();
        testPlan2.setId(testPlan1.getId());
        assertThat(testPlan1).isEqualTo(testPlan2);
        testPlan2.setId(2L);
        assertThat(testPlan1).isNotEqualTo(testPlan2);
        testPlan1.setId(null);
        assertThat(testPlan1).isNotEqualTo(testPlan2);
    }
}
