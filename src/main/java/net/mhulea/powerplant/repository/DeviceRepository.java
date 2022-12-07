package net.mhulea.powerplant.repository;

import net.mhulea.powerplant.entity.Device;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {


    /**
     *  Using named graph to retrieve LAZY entities. NamedEntityGraph annotation required in Device entity class.
     * @param id
     * @return*/
    @EntityGraph(value = "graph.Device.measurements", type = EntityGraph.EntityGraphType.FETCH)
    Device getById(Long id);

    Optional<Device> getByExternalId(Long id);

//    /**
//     * Using ad-hoc entity graph to retrieve LAZY entities
//     * @param id
//     * @return
//     */
//    @EntityGraph(attributePaths = "{telemetryMeasurements}")
//    Device getById(Long id);

    /**
     * Using query to retrieve LAZY entities.
     * @param id
     * @return
     */
    @Query("SELECT d FROM Device d JOIN FETCH d.telemetryTimeseriesData WHERE d.id = (:id)")
    public Device findByIdAndFetchMeasurementsEagerly(@Param("id") Long id);

}