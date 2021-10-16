package com.maria.trip.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maria.trip.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TripGroupsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripGroups.class);
        TripGroups tripGroups1 = new TripGroups();
        tripGroups1.setId(1L);
        TripGroups tripGroups2 = new TripGroups();
        tripGroups2.setId(tripGroups1.getId());
        assertThat(tripGroups1).isEqualTo(tripGroups2);
        tripGroups2.setId(2L);
        assertThat(tripGroups1).isNotEqualTo(tripGroups2);
        tripGroups1.setId(null);
        assertThat(tripGroups1).isNotEqualTo(tripGroups2);
    }
}
