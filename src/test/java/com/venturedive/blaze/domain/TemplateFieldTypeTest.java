package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TemplateFieldTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TemplateFieldType.class);
        TemplateFieldType templateFieldType1 = new TemplateFieldType();
        templateFieldType1.setId(1L);
        TemplateFieldType templateFieldType2 = new TemplateFieldType();
        templateFieldType2.setId(templateFieldType1.getId());
        assertThat(templateFieldType1).isEqualTo(templateFieldType2);
        templateFieldType2.setId(2L);
        assertThat(templateFieldType1).isNotEqualTo(templateFieldType2);
        templateFieldType1.setId(null);
        assertThat(templateFieldType1).isNotEqualTo(templateFieldType2);
    }
}
