package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ApplicationTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ApplicationType.class);
        ApplicationType applicationType1 = new ApplicationType();
        applicationType1.setId(1L);
        ApplicationType applicationType2 = new ApplicationType();
        applicationType2.setId(applicationType1.getId());
        assertThat(applicationType1).isEqualTo(applicationType2);
        applicationType2.setId(2L);
        assertThat(applicationType1).isNotEqualTo(applicationType2);
        applicationType1.setId(null);
        assertThat(applicationType1).isNotEqualTo(applicationType2);
    }
}
