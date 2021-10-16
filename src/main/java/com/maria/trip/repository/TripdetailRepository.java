package com.maria.trip.repository;

import com.maria.trip.domain.Tripdetail;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Tripdetail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripdetailRepository extends JpaRepository<Tripdetail, Long> {
    @Query("select tripdetail from Tripdetail tripdetail where tripdetail.tripmaster.login = ?#{principal.username}")
    List<Tripdetail> findByTripmasterIsCurrentUser();
}
