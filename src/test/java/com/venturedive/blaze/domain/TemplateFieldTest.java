package com.venturedive.blaze.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.venturedive.blaze.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TemplateFieldTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TemplateField.class);
        TemplateField templateField1 = new TemplateField();
        templateField1.setId(1L);
        TemplateField templateField2 = new TemplateField();
        templateField2.setId(templateField1.getId());
        assertThat(templateField1).isEqualTo(templateField2);
        templateField2.setId(2L);
        assertThat(templateField1).isNotEqualTo(templateField2);
        templateField1.setId(null);
        assertThat(templateField1).isNotEqualTo(templateField2);
    }
}
