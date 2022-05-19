package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SensitiveDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SensitiveData.class);
        SensitiveData sensitiveData1 = new SensitiveData();
        sensitiveData1.setId(1L);
        SensitiveData sensitiveData2 = new SensitiveData();
        sensitiveData2.setId(sensitiveData1.getId());
        assertThat(sensitiveData1).isEqualTo(sensitiveData2);
        sensitiveData2.setId(2L);
        assertThat(sensitiveData1).isNotEqualTo(sensitiveData2);
        sensitiveData1.setId(null);
        assertThat(sensitiveData1).isNotEqualTo(sensitiveData2);
    }
}
