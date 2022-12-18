/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.mhulea.powerplant.repository;

import net.mhulea.powerplant.entity.Device;
import net.mhulea.powerplant.entity.TelemetryTimeseriesData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author mihai
 */
@Repository
public interface TelemetryTimeseriesRepository extends CrudRepository<TelemetryTimeseriesData, Integer> {
    
    Optional<List<TelemetryTimeseriesData>> findByValueGreaterThan(Long start);
    
    Optional<TelemetryTimeseriesData> findByStartAndSampleSizeAndMeasurementType(Long start, String sampleSize, String measurementType);

    Optional<TelemetryTimeseriesData> findFirstByDeviceAndSampleSizeAndValueNotNullOrderByStartDesc(Device d,String sampleSize);
}
