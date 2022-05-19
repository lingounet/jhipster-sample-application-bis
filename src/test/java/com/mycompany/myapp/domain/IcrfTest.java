package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IcrfTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Icrf.class);
        Icrf icrf1 = new Icrf();
        icrf1.setId(1L);
        Icrf icrf2 = new Icrf();
        icrf2.setId(icrf1.getId());
        assertThat(icrf1).isEqualTo(icrf2);
        icrf2.setId(2L);
        assertThat(icrf1).isNotEqualTo(icrf2);
        icrf1.setId(null);
        assertThat(icrf1).isNotEqualTo(icrf2);
    }
}
