package com.maria.trip.repository;

import com.maria.trip.domain.UserGovernmentID;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the UserGovernmentID entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserGovernmentIDRepository extends JpaRepository<UserGovernmentID, Long> {
    @Query("select userGovernmentID from UserGovernmentID userGovernmentID where userGovernmentID.user.login = ?#{principal.username}")
    List<UserGovernmentID> findByUserIsCurrentUser();
}
