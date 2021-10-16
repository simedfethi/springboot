package com.maria.trip.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maria.trip.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdressListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdressList.class);
        AdressList adressList1 = new AdressList();
        adressList1.setId(1L);
        AdressList adressList2 = new AdressList();
        adressList2.setId(adressList1.getId());
        assertThat(adressList1).isEqualTo(adressList2);
        adressList2.setId(2L);
        assertThat(adressList1).isNotEqualTo(adressList2);
        adressList1.setId(null);
        assertThat(adressList1).isNotEqualTo(adressList2);
    }
}
