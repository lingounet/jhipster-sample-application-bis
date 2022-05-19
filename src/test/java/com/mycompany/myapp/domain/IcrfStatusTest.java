package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IcrfStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IcrfStatus.class);
        IcrfStatus icrfStatus1 = new IcrfStatus();
        icrfStatus1.setId(1L);
        IcrfStatus icrfStatus2 = new IcrfStatus();
        icrfStatus2.setId(icrfStatus1.getId());
        assertThat(icrfStatus1).isEqualTo(icrfStatus2);
        icrfStatus2.setId(2L);
        assertThat(icrfStatus1).isNotEqualTo(icrfStatus2);
        icrfStatus1.setId(null);
        assertThat(icrfStatus1).isNotEqualTo(icrfStatus2);
    }
}
