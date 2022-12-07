package net.mhulea.powerplant.repository;

import net.mhulea.powerplant.entity.Device;
import net.mhulea.powerplant.entity.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {

    Optional<Plant> getByExternalId(Long id);

}