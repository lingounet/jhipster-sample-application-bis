package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AmlTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Aml.class);
        Aml aml1 = new Aml();
        aml1.setId(1L);
        Aml aml2 = new Aml();
        aml2.setId(aml1.getId());
        assertThat(aml1).isEqualTo(aml2);
        aml2.setId(2L);
        assertThat(aml1).isNotEqualTo(aml2);
        aml1.setId(null);
        assertThat(aml1).isNotEqualTo(aml2);
    }
}
