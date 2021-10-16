package com.maria.trip.repository;

import com.maria.trip.domain.Notifications;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Notifications entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
    @Query("select notifications from Notifications notifications where notifications.user.login = ?#{principal.username}")
    List<Notifications> findByUserIsCurrentUser();
}
