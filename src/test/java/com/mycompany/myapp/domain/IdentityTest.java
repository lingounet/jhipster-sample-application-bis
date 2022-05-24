package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IdentityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Identity.class);
        Identity identity1 = new Identity();
        identity1.setId(1L);
        Identity identity2 = new Identity();
        identity2.setId(identity1.getId());
        assertThat(identity1).isEqualTo(identity2);
        identity2.setId(2L);
        assertThat(identity1).isNotEqualTo(identity2);
        identity1.setId(null);
        assertThat(identity1).isNotEqualTo(identity2);
    }
}
