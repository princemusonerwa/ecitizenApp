package com.minaloc.gov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.minaloc.gov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UmuturageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Umuturage.class);
        Umuturage umuturage1 = new Umuturage();
        umuturage1.setId(1L);
        Umuturage umuturage2 = new Umuturage();
        umuturage2.setId(umuturage1.getId());
        assertThat(umuturage1).isEqualTo(umuturage2);
        umuturage2.setId(2L);
        assertThat(umuturage1).isNotEqualTo(umuturage2);
        umuturage1.setId(null);
        assertThat(umuturage1).isNotEqualTo(umuturage2);
    }
}
