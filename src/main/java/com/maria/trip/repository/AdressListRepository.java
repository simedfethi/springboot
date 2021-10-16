package com.maria.trip.repository;

import com.maria.trip.domain.AdressList;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the AdressList entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdressListRepository extends JpaRepository<AdressList, Long> {}
