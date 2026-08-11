package com.demo.jalrakhsa.repository;

import com.demo.jalrakhsa.entity.LocationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationDataRepo extends JpaRepository<LocationData,Long>
{

    List<LocationData> findByLocationNameContainingIgnoreCase(String name);

    java.util.Optional<LocationData> findFirstByLocationNameIgnoreCase(String locationName);
}
