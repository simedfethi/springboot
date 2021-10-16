package com.maria.trip.repository;

import com.maria.trip.domain.TripGroups;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the TripGroups entity.
 */
@Repository
public interface TripGroupsRepository extends JpaRepository<TripGroups, Long> {
    @Query(
        value = "select distinct tripGroups from TripGroups tripGroups left join fetch tripGroups.internalUsers",
        countQuery = "select count(distinct tripGroups) from TripGroups tripGroups"
    )
    Page<TripGroups> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct tripGroups from TripGroups tripGroups left join fetch tripGroups.internalUsers")
    List<TripGroups> findAllWithEagerRelationships();

    @Query("select tripGroups from TripGroups tripGroups left join fetch tripGroups.internalUsers where tripGroups.id =:id")
    Optional<TripGroups> findOneWithEagerRelationships(@Param("id") Long id);
}
