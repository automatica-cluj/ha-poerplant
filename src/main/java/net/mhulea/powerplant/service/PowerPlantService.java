/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package net.mhulea.powerplant.service;


import java.time.LocalDateTime;
import java.util.Optional;

import net.mhulea.auroraclient.apimodel.telemetry.FimerResponseTelemetryTimeseries;
import net.mhulea.auroraclient.client.EnumPathParamDataType;
import net.mhulea.auroraclient.client.EnumQueryParamSampleSize;
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



    /**
     * Collect data from installation date (or from when last data is available) until end date.
     *
     * @param endDate
     */
    public void collectTelemetryTimeseriesInstallationToDate(Long endDate, EnumPathParamDataType dataType, EnumQueryParamSampleSize sampleSize){

        Device d = devRepo.getByExternalId(Long.valueOf(fimerService.getDeviceId())).orElseThrow(()->new MeasurementsAppException("No device found."));
        LocalDateTime instalationDate = d.getPlant().getFirstReportedDate();
        Optional<TelemetryTimeseriesData> r =
                teleRepo.findFirstByDeviceAndSampleSizeAndValueNotNullOrderByStartDesc(d,EnumQueryParamSampleSize.HOUR.toString());

        Long startDate = r.map(t -> PowerplantTimeUtils.getDayFromUnixEpoch(r.get().getStart()))
                .orElse(PowerplantTimeUtils.getFormatedDate(instalationDate));

        if(endDate>startDate){
            collectTelemetryTimeseriesData(startDate,endDate,dataType, sampleSize);
        }else{
            throw new MeasurementsAppException("Cannot process provided start and end dates. End date should be higher than start date.");
        }

    }

    public void collectTelemetryTimeseriesData(Long startDate, Long endDate, EnumPathParamDataType dataType, EnumQueryParamSampleSize sampleSize) {

        FimerResponseTelemetryTimeseries response =
                fimerService.getFimerClient().getTelemetryTimeseriesData(
                        fimerService.getDeviceId(),
                        dataType,
                        sampleSize,
                        startDate,
                        endDate);
        LOG.info("RESPONSE=" + response);
        telemetryService.writeTelemetryData(
                Long.valueOf(fimerService.getDeviceId()),
                dataType,
                sampleSize,
                Optional.ofNullable(response.getResult()));
    }


}
