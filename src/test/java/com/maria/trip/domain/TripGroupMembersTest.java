package com.maria.trip.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maria.trip.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TripGroupMembersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripGroupMembers.class);
        TripGroupMembers tripGroupMembers1 = new TripGroupMembers();
        tripGroupMembers1.setId(1L);
        TripGroupMembers tripGroupMembers2 = new TripGroupMembers();
        tripGroupMembers2.setId(tripGroupMembers1.getId());
        assertThat(tripGroupMembers1).isEqualTo(tripGroupMembers2);
        tripGroupMembers2.setId(2L);
        assertThat(tripGroupMembers1).isNotEqualTo(tripGroupMembers2);
        tripGroupMembers1.setId(null);
        assertThat(tripGroupMembers1).isNotEqualTo(tripGroupMembers2);
    }
}
