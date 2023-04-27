package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestCasePriorityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestCasePriority.class);
        TestCasePriority testCasePriority1 = new TestCasePriority();
        testCasePriority1.setId(1L);
        TestCasePriority testCasePriority2 = new TestCasePriority();
        testCasePriority2.setId(testCasePriority1.getId());
        assertThat(testCasePriority1).isEqualTo(testCasePriority2);
        testCasePriority2.setId(2L);
        assertThat(testCasePriority1).isNotEqualTo(testCasePriority2);
        testCasePriority1.setId(null);
        assertThat(testCasePriority1).isNotEqualTo(testCasePriority2);
    }
}
