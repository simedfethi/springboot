package com.maria.trip.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maria.trip.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserGovernmentIDTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserGovernmentID.class);
        UserGovernmentID userGovernmentID1 = new UserGovernmentID();
        userGovernmentID1.setId(1L);
        UserGovernmentID userGovernmentID2 = new UserGovernmentID();
        userGovernmentID2.setId(userGovernmentID1.getId());
        assertThat(userGovernmentID1).isEqualTo(userGovernmentID2);
        userGovernmentID2.setId(2L);
        assertThat(userGovernmentID1).isNotEqualTo(userGovernmentID2);
        userGovernmentID1.setId(null);
        assertThat(userGovernmentID1).isNotEqualTo(userGovernmentID2);
    }
}
