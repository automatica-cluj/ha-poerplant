/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.mhulea.powerplant.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.mhulea.powerplant.entity.Device;
import org.hibernate.Hibernate;
import org.springframework.lang.Nullable;

import java.util.Objects;

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
    private String measurementType;
    private String valueType;
    private Long start;
    private String units;
    private Double value;
    private String sampleSize;
    
    @ManyToOne 
    @JoinColumn(name="device_id")
    private Device device;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TelemetryTimeseriesData that = (TelemetryTimeseriesData) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
