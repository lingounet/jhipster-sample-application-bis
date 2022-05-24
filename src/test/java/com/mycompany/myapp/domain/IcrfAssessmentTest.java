package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IcrfAssessmentTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IcrfAssessment.class);
        IcrfAssessment icrfAssessment1 = new IcrfAssessment();
        icrfAssessment1.setId(1L);
        IcrfAssessment icrfAssessment2 = new IcrfAssessment();
        icrfAssessment2.setId(icrfAssessment1.getId());
        assertThat(icrfAssessment1).isEqualTo(icrfAssessment2);
        icrfAssessment2.setId(2L);
        assertThat(icrfAssessment1).isNotEqualTo(icrfAssessment2);
        icrfAssessment1.setId(null);
        assertThat(icrfAssessment1).isNotEqualTo(icrfAssessment2);
    }
}
