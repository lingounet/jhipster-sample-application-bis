package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SecurityInterviewTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SecurityInterview.class);
        SecurityInterview securityInterview1 = new SecurityInterview();
        securityInterview1.setId(1L);
        SecurityInterview securityInterview2 = new SecurityInterview();
        securityInterview2.setId(securityInterview1.getId());
        assertThat(securityInterview1).isEqualTo(securityInterview2);
        securityInterview2.setId(2L);
        assertThat(securityInterview1).isNotEqualTo(securityInterview2);
        securityInterview1.setId(null);
        assertThat(securityInterview1).isNotEqualTo(securityInterview2);
    }
}
