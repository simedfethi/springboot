package com.maria.trip.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maria.trip.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserPhoneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPhone.class);
        UserPhone userPhone1 = new UserPhone();
        userPhone1.setId(1L);
        UserPhone userPhone2 = new UserPhone();
        userPhone2.setId(userPhone1.getId());
        assertThat(userPhone1).isEqualTo(userPhone2);
        userPhone2.setId(2L);
        assertThat(userPhone1).isNotEqualTo(userPhone2);
        userPhone1.setId(null);
        assertThat(userPhone1).isNotEqualTo(userPhone2);
    }
}
