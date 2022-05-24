package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HostingTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HostingType.class);
        HostingType hostingType1 = new HostingType();
        hostingType1.setId(1L);
        HostingType hostingType2 = new HostingType();
        hostingType2.setId(hostingType1.getId());
        assertThat(hostingType1).isEqualTo(hostingType2);
        hostingType2.setId(2L);
        assertThat(hostingType1).isNotEqualTo(hostingType2);
        hostingType1.setId(null);
        assertThat(hostingType1).isNotEqualTo(hostingType2);
    }
}
