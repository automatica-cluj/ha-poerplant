/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeautomation.repository;

import homeautomation.entity.TelemetryTimeseriesData;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mihai
 */
@Repository
public interface TelemetryTimeseriesRepository extends CrudRepository<TelemetryTimeseriesData, Integer>{
    
    Optional<List<TelemetryTimeseriesData>> findByValueGreaterThan(Long start);
    
    Optional<TelemetryTimeseriesData> findByStartAndSampleSize(Long start, String sampleSize);
    
}
