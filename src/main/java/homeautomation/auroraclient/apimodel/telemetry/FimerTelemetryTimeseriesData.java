/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package homeautomation.auroraclient.apimodel.telemetry;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.Nullable;

/**
 * Used for both aggregated and timeseries. 
 * @author mihai
 */

@Getter @Setter @ToString
public class FimerTelemetryTimeseriesData {
    private Long start;
    private String units;
    @Nullable
    private Double value;
}
