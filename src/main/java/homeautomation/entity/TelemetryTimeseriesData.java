/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeautomation.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

/**
 *
 * @author mihai
 */
@Entity
@Table(
        name="telemetry_timeseries",
        uniqueConstraints =
        { //other constraints
                @UniqueConstraint(name = "UniqueStartAndSampleSize", columnNames = { "start", "sampleSize" })}
)

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class TelemetryTimeseriesData {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;
    private String dataType;
    private Long start;
    private String units;
    private Double value;
    private String sampleSize;
    
    @ManyToOne 
    @JoinColumn(name="device_id")
    private Device device;

}
