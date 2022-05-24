package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalDataTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalData.class);
        PersonalData personalData1 = new PersonalData();
        personalData1.setId(1L);
        PersonalData personalData2 = new PersonalData();
        personalData2.setId(personalData1.getId());
        assertThat(personalData1).isEqualTo(personalData2);
        personalData2.setId(2L);
        assertThat(personalData1).isNotEqualTo(personalData2);
        personalData1.setId(null);
        assertThat(personalData1).isNotEqualTo(personalData2);
    }
}
