package com.maria.trip.repository;

import com.maria.trip.domain.TripCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TripCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripCategoryRepository extends JpaRepository<TripCategory, Long> {}
