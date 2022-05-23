package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PsatTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Psat.class);
        Psat psat1 = new Psat();
        psat1.setId(1L);
        Psat psat2 = new Psat();
        psat2.setId(psat1.getId());
        assertThat(psat1).isEqualTo(psat2);
        psat2.setId(2L);
        assertThat(psat1).isNotEqualTo(psat2);
        psat1.setId(null);
        assertThat(psat1).isNotEqualTo(psat2);
    }
}
