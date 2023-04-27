package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TestLevelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TestLevel.class);
        TestLevel testLevel1 = new TestLevel();
        testLevel1.setId(1L);
        TestLevel testLevel2 = new TestLevel();
        testLevel2.setId(testLevel1.getId());
        assertThat(testLevel1).isEqualTo(testLevel2);
        testLevel2.setId(2L);
        assertThat(testLevel1).isNotEqualTo(testLevel2);
        testLevel1.setId(null);
        assertThat(testLevel1).isNotEqualTo(testLevel2);
    }
}
