package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AvailabilityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Availability.class);
        Availability availability1 = new Availability();
        availability1.setId(1L);
        Availability availability2 = new Availability();
        availability2.setId(availability1.getId());
        assertThat(availability1).isEqualTo(availability2);
        availability2.setId(2L);
        assertThat(availability1).isNotEqualTo(availability2);
        availability1.setId(null);
        assertThat(availability1).isNotEqualTo(availability2);
    }
}
