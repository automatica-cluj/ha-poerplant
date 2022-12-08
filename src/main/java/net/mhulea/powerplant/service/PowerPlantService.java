/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.mhulea.powerplant.service;


import java.time.LocalDateTime;
import java.util.Optional;

import net.mhulea.auroraclient.apimodel.telemetry.FimerResponseTelemetryTimeseries;
import net.mhulea.auroraclient.exception.FimerClinetException;
import net.mhulea.auroraclient.util.EnumQueryParamSampleSize;
import net.mhulea.powerplant.entity.Device;
import net.mhulea.powerplant.entity.TelemetryTimeseriesData;
import net.mhulea.powerplant.exceptions.MeasurementsAppException;
import net.mhulea.powerplant.repository.DeviceRepository;
import net.mhulea.powerplant.repository.TelemetryTimeseriesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mihai
 */

@Service
public class PowerPlantService {

    private static final Logger LOG = LoggerFactory.getLogger(PowerPlantService.class);

    @Autowired
    FimerClientService fimerService;

    @Autowired
    private TelemetryService telemetryService;

    @Autowired
    private DeviceRepository devRepo;

    @Autowired
    private TelemetryTimeseriesRepository teleRepo;

    public void collectTelemetryTimeseriesDataGeneric(String measurementType, String dataType, String valueType, String sampleSize, Long startDate, Long endDate) throws FimerClinetException {
        Device d = devRepo.getByExternalId(Long.valueOf(fimerService.getDeviceId())).orElseThrow(()->new MeasurementsAppException("No active device found."));
        if(startDate==null&endDate!=null){ //no start date is provided collect data from installation date till present
            LocalDateTime instalationDate = d.getPlant().getFirstReportedDate();
            Optional<TelemetryTimeseriesData> r =
                    teleRepo.findFirstByDeviceAndSampleSizeAndValueNotNullOrderByStartDesc(d,EnumQueryParamSampleSize.HOUR.toString());

            startDate = r.map(t -> PowerplantTimeUtils.getDayFromUnixEpoch(r.get().getStart()))
                    .orElse(PowerplantTimeUtils.getFormatedDate(instalationDate));
        }

        if(startDate==null && endDate==null){ //no date is provided, collect data for today only
            startDate = PowerplantTimeUtils.getToday();
            endDate = PowerplantTimeUtils.getDayRelativeToToday(1);
        }

        if(startDate>endDate)
            throw new MeasurementsAppException("Cannot process request. End date should be higher than start date.");

        FimerResponseTelemetryTimeseries response =  fimerService.getFimerClient().
                getTelemetryTimeseriesDataGeneric(d.getExternalId().toString(),measurementType,valueType,dataType,sampleSize,startDate,endDate);

        telemetryService.writeTelemetryData(
                Long.valueOf(fimerService.getDeviceId()),
                dataType,
                measurementType,
                valueType,
                sampleSize,
                startDate,
                endDate,
                Optional.ofNullable(response.getResult()));
    }
}
