package com.maria.trip.repository;

import com.maria.trip.domain.UserPreferences;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserPreferences entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserPreferencesRepository extends JpaRepository<UserPreferences, Long> {
    @Query("select userPreferences from UserPreferences userPreferences where userPreferences.internalUser.login = ?#{principal.username}")
    List<UserPreferences> findByInternalUserIsCurrentUser();
}
