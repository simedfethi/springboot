package com.maria.trip.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.maria.trip.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserPreferencesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserPreferences.class);
        UserPreferences userPreferences1 = new UserPreferences();
        userPreferences1.setId(1L);
        UserPreferences userPreferences2 = new UserPreferences();
        userPreferences2.setId(userPreferences1.getId());
        assertThat(userPreferences1).isEqualTo(userPreferences2);
        userPreferences2.setId(2L);
        assertThat(userPreferences1).isNotEqualTo(userPreferences2);
        userPreferences1.setId(null);
        assertThat(userPreferences1).isNotEqualTo(userPreferences2);
    }
}
