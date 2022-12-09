package net.mhulea.powerplant.controller;

import io.swagger.v3.oas.annotations.Parameter;
import net.mhulea.auroraclient.exception.FimerClinetException;
import net.mhulea.powerplant.service.PowerPlantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TelemetryController {

    @Autowired
    private PowerPlantService plantService;

    /**
     * Update telemetry data based on specific parameters starting from initial reporting date till present.
     *
     * @param measurementType
     * @param dataType
     * @param valueType
     * @param sampleSize
     * @return
     */
    @GetMapping("/stats/timeseries/{measurementType}/{dataType}/{valueType}")
    public ResponseEntity<PowerplantResponse> requestUpdateTelemetryData( @Parameter(required = true, description = "Possible values: energy, current, temperature, wind, frequency, voltage, power")
                                                                  @PathVariable("measurementType") String measurementType,
                                                              @Parameter(required = true, description = "Possible values for energy: GenerationEnergy, DCGenerationEnergy, Insolation, StorageInEnergy, StorageOutEnergy, GridEnergyExport, GridEnergyImport, SelfConsumedEnergy, ActiveEnergyEV, SessionEnergyEV <br> Possible values for current : Current, DCCurrent <br> Possible values for temperature : CellTemp, AmbientTemp <br> Possible values for wind: WindDirection, WindSpeed <br> Possible values for frequency: LineFrequency <br> Possible values for voltage: Voltage, DCVoltage <br> Possible values for power: GenerationPower, DCGenerationPower, Irradiance, GridPowerExport, StoredPower, ActivePowerEV")
                                                                @PathVariable("dataType") String dataType,
                                                              @Parameter(required = true, description = "Possible values (all except energy): maximum, minimum, average <br> Possible values (only for energy): cumulative, delta")
                                                                  @PathVariable("valueType") String valueType,
                                                              @Parameter(required = true, description = "Sampling rate of data(s) for the API call(s).<br>Possible values : Min5, Min15, Hour, Day, Month, Year ")
                                                                  @RequestParam("sampleSize") String sampleSize,
                                                              @Parameter(required = false, description = "Start date for collecting measurements. Pattern: yyyyMMdd  ")
                                                                  @RequestParam(required = false,name="startDate") Long startDate,
                                                              @Parameter(required = false, description = "End date for collecting measurements. Pattern: yyyyMMdd ")
                                                                  @RequestParam(required = false,name="endDate") Long endDate

                                             ) {

        try {
            plantService.collectTelemetryTimeseriesDataGeneric(measurementType, dataType, valueType,sampleSize, startDate, endDate);
            return new ResponseEntity<>(
                    new PowerplantResponse("Request processed!"),
                    HttpStatus.OK);
        } catch (FimerClinetException e) {
            return new ResponseEntity<>(
                    new PowerplantResponse(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
