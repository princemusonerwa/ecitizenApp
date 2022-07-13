package com.minaloc.gov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.minaloc.gov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UmurimoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Umurimo.class);
        Umurimo umurimo1 = new Umurimo();
        umurimo1.setId(1L);
        Umurimo umurimo2 = new Umurimo();
        umurimo2.setId(umurimo1.getId());
        assertThat(umurimo1).isEqualTo(umurimo2);
        umurimo2.setId(2L);
        assertThat(umurimo1).isNotEqualTo(umurimo2);
        umurimo1.setId(null);
        assertThat(umurimo1).isNotEqualTo(umurimo2);
    }
}
