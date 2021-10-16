package com.maria.trip.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maria.trip.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TripCategoryTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TripCategory.class);
        TripCategory tripCategory1 = new TripCategory();
        tripCategory1.setId(1L);
        TripCategory tripCategory2 = new TripCategory();
        tripCategory2.setId(tripCategory1.getId());
        assertThat(tripCategory1).isEqualTo(tripCategory2);
        tripCategory2.setId(2L);
        assertThat(tripCategory1).isNotEqualTo(tripCategory2);
        tripCategory1.setId(null);
        assertThat(tripCategory1).isNotEqualTo(tripCategory2);
    }
}
