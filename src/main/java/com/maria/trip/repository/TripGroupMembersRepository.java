package com.maria.trip.repository;

import com.maria.trip.domain.TripGroupMembers;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TripGroupMembers entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TripGroupMembersRepository extends JpaRepository<TripGroupMembers, Long> {}
