package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HostingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hosting.class);
        Hosting hosting1 = new Hosting();
        hosting1.setId(1L);
        Hosting hosting2 = new Hosting();
        hosting2.setId(hosting1.getId());
        assertThat(hosting1).isEqualTo(hosting2);
        hosting2.setId(2L);
        assertThat(hosting1).isNotEqualTo(hosting2);
        hosting1.setId(null);
        assertThat(hosting1).isNotEqualTo(hosting2);
    }
}
