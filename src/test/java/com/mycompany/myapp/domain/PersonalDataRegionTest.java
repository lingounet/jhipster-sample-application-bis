package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalDataRegionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalDataRegion.class);
        PersonalDataRegion personalDataRegion1 = new PersonalDataRegion();
        personalDataRegion1.setId(1L);
        PersonalDataRegion personalDataRegion2 = new PersonalDataRegion();
        personalDataRegion2.setId(personalDataRegion1.getId());
        assertThat(personalDataRegion1).isEqualTo(personalDataRegion2);
        personalDataRegion2.setId(2L);
        assertThat(personalDataRegion1).isNotEqualTo(personalDataRegion2);
        personalDataRegion1.setId(null);
        assertThat(personalDataRegion1).isNotEqualTo(personalDataRegion2);
    }
}
