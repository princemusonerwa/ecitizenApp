package com.minaloc.gov.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.minaloc.gov.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UmuyoboziTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Umuyobozi.class);
        Umuyobozi umuyobozi1 = new Umuyobozi();
        umuyobozi1.setId(1L);
        Umuyobozi umuyobozi2 = new Umuyobozi();
        umuyobozi2.setId(umuyobozi1.getId());
        assertThat(umuyobozi1).isEqualTo(umuyobozi2);
        umuyobozi2.setId(2L);
        assertThat(umuyobozi1).isNotEqualTo(umuyobozi2);
        umuyobozi1.setId(null);
        assertThat(umuyobozi1).isNotEqualTo(umuyobozi2);
    }
}
