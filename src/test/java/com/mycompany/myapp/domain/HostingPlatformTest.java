package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class HostingPlatformTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HostingPlatform.class);
        HostingPlatform hostingPlatform1 = new HostingPlatform();
        hostingPlatform1.setId(1L);
        HostingPlatform hostingPlatform2 = new HostingPlatform();
        hostingPlatform2.setId(hostingPlatform1.getId());
        assertThat(hostingPlatform1).isEqualTo(hostingPlatform2);
        hostingPlatform2.setId(2L);
        assertThat(hostingPlatform1).isNotEqualTo(hostingPlatform2);
        hostingPlatform1.setId(null);
        assertThat(hostingPlatform1).isNotEqualTo(hostingPlatform2);
    }
}
