package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PersonalDataTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PersonalDataType.class);
        PersonalDataType personalDataType1 = new PersonalDataType();
        personalDataType1.setId(1L);
        PersonalDataType personalDataType2 = new PersonalDataType();
        personalDataType2.setId(personalDataType1.getId());
        assertThat(personalDataType1).isEqualTo(personalDataType2);
        personalDataType2.setId(2L);
        assertThat(personalDataType1).isNotEqualTo(personalDataType2);
        personalDataType1.setId(null);
        assertThat(personalDataType1).isNotEqualTo(personalDataType2);
    }
}
