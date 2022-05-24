package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ComplementaryQuestionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ComplementaryQuestion.class);
        ComplementaryQuestion complementaryQuestion1 = new ComplementaryQuestion();
        complementaryQuestion1.setId(1L);
        ComplementaryQuestion complementaryQuestion2 = new ComplementaryQuestion();
        complementaryQuestion2.setId(complementaryQuestion1.getId());
        assertThat(complementaryQuestion1).isEqualTo(complementaryQuestion2);
        complementaryQuestion2.setId(2L);
        assertThat(complementaryQuestion1).isNotEqualTo(complementaryQuestion2);
        complementaryQuestion1.setId(null);
        assertThat(complementaryQuestion1).isNotEqualTo(complementaryQuestion2);
    }
}
