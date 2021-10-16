package com.maria.trip.repository;

import com.maria.trip.domain.ApplicationUser;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the ApplicationUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Long> {
    @Query("select applicationUser from ApplicationUser applicationUser where applicationUser.internalUser.login = ?#{principal.username}")
    List<ApplicationUser> findByInternalUserIsCurrentUser();
}
