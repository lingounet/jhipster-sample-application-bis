package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SensitiveDataTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SensitiveDataType.class);
        SensitiveDataType sensitiveDataType1 = new SensitiveDataType();
        sensitiveDataType1.setId(1L);
        SensitiveDataType sensitiveDataType2 = new SensitiveDataType();
        sensitiveDataType2.setId(sensitiveDataType1.getId());
        assertThat(sensitiveDataType1).isEqualTo(sensitiveDataType2);
        sensitiveDataType2.setId(2L);
        assertThat(sensitiveDataType1).isNotEqualTo(sensitiveDataType2);
        sensitiveDataType1.setId(null);
        assertThat(sensitiveDataType1).isNotEqualTo(sensitiveDataType2);
    }
}
