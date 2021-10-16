package com.maria.trip.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maria.trip.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TripdetailTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tripdetail.class);
        Tripdetail tripdetail1 = new Tripdetail();
        tripdetail1.setId(1L);
        Tripdetail tripdetail2 = new Tripdetail();
        tripdetail2.setId(tripdetail1.getId());
        assertThat(tripdetail1).isEqualTo(tripdetail2);
        tripdetail2.setId(2L);
        assertThat(tripdetail1).isNotEqualTo(tripdetail2);
        tripdetail1.setId(null);
        assertThat(tripdetail1).isNotEqualTo(tripdetail2);
    }
}
